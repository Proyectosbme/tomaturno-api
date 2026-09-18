package com.empresa.tomaturno.framework.adapters.config;

import java.time.Duration;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.empresa.tomaturno.estadooperador.application.command.port.input.EstadoOperadorCommandInputPort;
import com.empresa.tomaturno.estadooperador.application.query.port.input.EstadoOperadorQueryInputPort;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleEstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleTipoDescanso;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Detecta cuando un operador con caja ACTIVA se queda sin ninguna sesión de WebSocket
 * conectada (cerró el navegador, perdió la sesión, se cayó la red) y, pasado un margen de
 * tolerancia, lo pasa automáticamente a un descanso especial (SESION_CERRADA) que el
 * operador no puede elegir por su cuenta: así deja de contar como tiempo activo y deja de
 * recibir turnos nuevos (el llamado/asignación automática ya exige estado ACTIVA) mientras
 * está desconectado. Al reconectar, lo regresa a ACTIVA sin que tenga que hacer nada.
 *
 * No depende de TurnoWebSocket (quien sí depende de esta clase, para no crear un ciclo):
 * "¿sigue conectado?" se lo pasan como una función al momento de programar la revisión.
 */
@ApplicationScoped
public class EstadoOperadorAutomaticoOrquestador {

    private static final Logger LOG = Logger.getLogger(EstadoOperadorAutomaticoOrquestador.class.getName());

    /** Margen de tolerancia antes de considerar la desconexión real y no un corte breve de red. */
    private static final long ESPERA_SEGUNDOS = 20;

    private final EstadoOperadorCommandInputPort estadoOperadorCommandInputPort;
    private final EstadoOperadorQueryInputPort estadoOperadorQueryInputPort;

    public EstadoOperadorAutomaticoOrquestador(EstadoOperadorCommandInputPort estadoOperadorCommandInputPort,
            EstadoOperadorQueryInputPort estadoOperadorQueryInputPort) {
        this.estadoOperadorCommandInputPort = estadoOperadorCommandInputPort;
        this.estadoOperadorQueryInputPort = estadoOperadorQueryInputPort;
    }

    /**
     * Se llama cuando un operador se queda sin ninguna pestaña conectada. Programa una
     * revisión ESPERA_SEGUNDOS después: si para entonces sigue desconectado (según
     * siguesConectado) y sigue ACTIVA, lo pasa a descanso SESION_CERRADA. Si reconectó
     * antes, no hace falta "cancelar" nada: la revisión simplemente no encuentra motivo
     * para actuar.
     *
     * La espera corre sobre el executor de Mutiny (Infrastructure), no sobre un hilo
     * propio: ese executor lo administra Quarkus y se recicla correctamente en cada
     * hot-reload del modo dev. Un ScheduledExecutorService creado a mano sobrevive a los
     * reloads y termina usando clases de un classloader viejo (ClassCastException).
     */
    public void operadorDesconectado(Long idUsuario, Long idSucursal, BooleanSupplier siguesConectado) {
        if (idUsuario == null || idSucursal == null) {
            return;
        }
        Uni.createFrom().nullItem()
                .onItem().delayIt().by(Duration.ofSeconds(ESPERA_SEGUNDOS))
                .subscribe().with(
                        ignorado -> revisarDesconexion(idUsuario, idSucursal, siguesConectado),
                        fallo -> LOG.log(Level.WARNING, fallo,
                                () -> "Error programando revisión de desconexión (idUsuario=" + idUsuario + ")"));
    }

    private void revisarDesconexion(Long idUsuario, Long idSucursal, BooleanSupplier siguesConectado) {
        // Esto corre en el executor de Mutiny, fuera de cualquier transacción de Quarkus:
        // hay que abrir una explícitamente para poder leer/escribir con Panache acá.
        QuarkusTransaction.requiringNew().run(() -> {
            try {
                if (siguesConectado.getAsBoolean()) {
                    return; // reconectó dentro del margen de tolerancia
                }
                EstadoOperador vigente = estadoOperadorQueryInputPort.buscarVigente(idUsuario, idSucursal);
                // Si ya no está ACTIVA (se cerró, entró a descanso manual, etc.) no corresponde:
                // solo se convierte en SESION_CERRADA la desconexión de alguien que seguía activo.
                if (!estaActiva(vigente)) {
                    return;
                }
                estadoOperadorCommandInputPort.iniciarDescanso(idUsuario, idSucursal, vigente.getIdPuesto(),
                        DetalleTipoDescanso.SESION_CERRADA.getValor(), null);
            } catch (Exception e) {
                LOG.log(Level.WARNING, e,
                        () -> "Error revisando desconexión de operador (idUsuario=" + idUsuario + ")");
            }
        });
    }

    private boolean estaActiva(EstadoOperador vigente) {
        return vigente != null
                && vigente.getIdEstadoOperador() != null
                && vigente.getIdEstadoOperador().equals(DetalleEstadoOperador.ACTIVA.getValor());
    }

    /**
     * Se llama cuando un operador reconecta (abre una pestaña). Si el motivo de su
     * descanso actual es justo esta desconexión automática, lo regresa a ACTIVA sin que
     * tenga que hacer nada. Si está en cualquier otro estado (ACTIVA, CERRADA, o un
     * descanso manual elegido por él), no le toca el estado.
     */
    public void operadorReconectado(Long idUsuario, Long idSucursal) {
        if (idUsuario == null || idSucursal == null) {
            return;
        }
        // onOpen se ejecuta en el hilo de I/O de Vert.x: una consulta a la base ahí mismo
        // dispara BlockingOperationNotAllowedException. Se despacha al worker pool de
        // Mutiny (mismo motivo que en operadorDesconectado: administrado por Quarkus,
        // sobrevive bien al hot-reload del modo dev).
        Uni.createFrom().nullItem()
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .subscribe().with(
                        ignorado -> reactivarSiCorresponde(idUsuario, idSucursal),
                        fallo -> LOG.log(Level.WARNING, fallo,
                                () -> "Error programando reactivación tras reconexión (idUsuario=" + idUsuario + ")"));
    }

    private void reactivarSiCorresponde(Long idUsuario, Long idSucursal) {
        QuarkusTransaction.requiringNew().run(() -> {
            try {
                EstadoOperador vigente = estadoOperadorQueryInputPort.buscarVigente(idUsuario, idSucursal);
                // Solo actúa si el motivo de descanso vigente es justo este automático: si está
                // ACTIVA, CERRADA, o en un descanso manual (comida, baño, otro), no le toca el estado.
                if (!estaEnDescansoPorSesionCerrada(vigente)) {
                    return;
                }
                estadoOperadorCommandInputPort.quitarDescanso(idUsuario, idSucursal, vigente.getIdPuesto());
            } catch (Exception e) {
                LOG.log(Level.WARNING, e,
                        () -> "Error reactivando operador tras reconexión (idUsuario=" + idUsuario + ")");
            }
        });
    }

    private boolean estaEnDescansoPorSesionCerrada(EstadoOperador vigente) {
        return vigente != null
                && vigente.getIdEstadoOperador() != null
                && vigente.getIdTipoDescanso() != null
                && vigente.getIdEstadoOperador().equals(DetalleEstadoOperador.DESCANSO.getValor())
                && vigente.getIdTipoDescanso().equals(DetalleTipoDescanso.SESION_CERRADA.getValor());
    }
}
