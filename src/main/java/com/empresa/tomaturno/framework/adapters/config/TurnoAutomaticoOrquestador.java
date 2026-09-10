package com.empresa.tomaturno.framework.adapters.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.empresa.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.input.DetalleColaxPuestoQueryInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.estadooperador.application.query.port.input.EstadoOperadorQueryInputPort;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleEstadoOperador;
import com.empresa.tomaturno.framework.adapters.input.dto.TurnoResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.TurnoInputMapper;
import com.empresa.tomaturno.framework.adapters.input.controller.TurnoWebSocket;
import com.empresa.tomaturno.turno.application.command.port.input.TurnoCommandInputPort;
import com.empresa.tomaturno.turno.application.query.port.input.TurnoQueryInputPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.vo.DetalleEstado;
import com.empresa.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;

/**
 * Orquesta el "llamado automático" del siguiente turno pendiente de un puesto,
 * conectando estado de operador (EstadoOperador), configuración por sucursal
 * (TURNO_AUTOMATICO) y el flujo normal de turnos (TurnoCommandInputPort).
 *
 * No se ocupa del caso "turno nuevo creado con cola vacía" (eso lo maneja otro
 * flujo).
 */
@ApplicationScoped
public class TurnoAutomaticoOrquestador {

    private static final Logger LOG = Logger.getLogger(TurnoAutomaticoOrquestador.class.getName());
    private static final String NOMBRE_CONFIGURACION = "TURNO_AUTOMATICO";

    private final TurnoCommandInputPort turnoCommandInputPort;
    private final TurnoQueryInputPort turnoQueryInputPort;
    private final TurnoInputMapper turnoInputMapper;
    private final TurnoWebSocket turnoWebSocket;
    private final Jsonb jsonb;
    private final ConfiguracionQueryInputPort configuracionQueryInputPort;
    private final EstadoOperadorQueryInputPort estadoOperadorQueryInputPort;
    private final DetalleColaxPuestoQueryInputPort detalleColaxPuestoQueryInputPort;
    private final UsuarioQueryInputPort usuarioQueryInputPort;

    public TurnoAutomaticoOrquestador(TurnoCommandInputPort turnoCommandInputPort,
            TurnoQueryInputPort turnoQueryInputPort,
            TurnoInputMapper turnoInputMapper,
            TurnoWebSocket turnoWebSocket,
            Jsonb jsonb,
            ConfiguracionQueryInputPort configuracionQueryInputPort,
            EstadoOperadorQueryInputPort estadoOperadorQueryInputPort,
            DetalleColaxPuestoQueryInputPort detalleColaxPuestoQueryInputPort,
            UsuarioQueryInputPort usuarioQueryInputPort) {
        this.turnoCommandInputPort = turnoCommandInputPort;
        this.turnoQueryInputPort = turnoQueryInputPort;
        this.turnoInputMapper = turnoInputMapper;
        this.turnoWebSocket = turnoWebSocket;
        this.jsonb = jsonb;
        this.configuracionQueryInputPort = configuracionQueryInputPort;
        this.estadoOperadorQueryInputPort = estadoOperadorQueryInputPort;
        this.detalleColaxPuestoQueryInputPort = detalleColaxPuestoQueryInputPort;
        this.usuarioQueryInputPort = usuarioQueryInputPort;
    }

    private String wsPayload(String event, Long idSucursal, TurnoResponseDTO dto) {
        return "{\"event\":\"" + event + "\",\"idSucursal\":" + idSucursal + ",\"turno\":" + jsonb.toJson(dto)
                + "}";
    }

    /**
     * Si el puesto indicado tiene actualmente un turno en estado LLAMADO, lo
     * finaliza
     * y emite el evento websocket TURNO_FINALIZADO. Si no hay turno LLAMADO, no
     * hace nada.
     * Tolerante a nulls: si falta algún dato necesario, no hace nada.
     */
    public void finalizarTurnoActivoSiExiste(Long idSucursal, Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        if (idSucursal == null || idPuesto == null || idSucursalPuesto == null) {
            return;
        }

        Integer estadoLlamado = (int) DetalleEstado.LLAMADO.getValor();
        List<Turno> llamados = turnoQueryInputPort.buscarPorFiltro(
                idSucursal, null, null, estadoLlamado, LocalDate.now(), idPuesto, idSucursalPuesto);

        if (llamados == null || llamados.isEmpty()) {
            return;
        }

        Turno turno = llamados.stream()
                .filter(t -> idUsuario.equals(t.getIdUsuario()))
                .findFirst()
                .orElse(null);

        if (turno == null) {
            return; 
        }

        Turno finalizado = turnoCommandInputPort.finalizar(turno.getIdSucursal(), turno.getFechaCreacion(),
                turno.getCodigoTurno());
        TurnoResponseDTO responseDTO = turnoInputMapper.toResponse(finalizado);
        turnoWebSocket.enviarTurno(wsPayload("TURNO_FINALIZADO", idSucursal, responseDTO));
    }

    /**
     * Si la sucursal tiene activado TURNO_AUTOMATICO y el estado del operador sigue
     * ACTIVA,
     * intenta llamar el siguiente turno pendiente del puesto. Si no hay turnos
     * pendientes,
     * no hace nada (caso normal, no es error). Tolerante a nulls.
     */
    public void intentarLlamadoAutomatico(Long idSucursal, Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        if (idSucursal == null || idPuesto == null || idSucursalPuesto == null || idUsuario == null) {
            return;
        }

        Configuracion cfg = configuracionQueryInputPort.buscarPorNombre(idSucursal, NOMBRE_CONFIGURACION);
        if (cfg == null || cfg.getParametro() == null || cfg.getParametro() != 1) {
            return;
        }

        EstadoOperador vigente = estadoOperadorQueryInputPort.buscarVigente(idUsuario, idSucursal);
        if (vigente == null || vigente.getIdEstadoOperador() == null
                || !vigente.getIdEstadoOperador().equals(DetalleEstadoOperador.ACTIVA.getValor())) {
            return;
        }

        if (!turnoWebSocket.tieneSesionActiva(idUsuario)) {
            LOG.log(Level.FINE,
                    "Operador ACTIVA pero sin sesión de WebSocket, se omite el llamado automático (idUsuario={0})",
                    idUsuario);
            return;
        }

        Turno turno;
        try {
            turno = turnoCommandInputPort.llamarSiguiente(idSucursal, idPuesto, idSucursalPuesto, idUsuario);
        } catch (TurnoNotFoundException e) {
            LOG.log(Level.FINE, "No hay turnos pendientes para el llamado automático (idSucursal={0}, idPuesto={1})",
                    new Object[] { idSucursal, idPuesto });
            return;
        }

        TurnoResponseDTO responseDTO = turnoInputMapper.toResponse(turno);
        turnoWebSocket.enviarTurno(wsPayload("TURNO_LLAMADO", idSucursal, responseDTO));
    }

    /** Candidato elegible para recibir el turno recién creado. */
    private record CandidatoAsignacion(Long idUsuario, Long idPuesto, Long idSucursalPuesto,
            LocalDateTime ultimaFecha) {
    }

    /**
     * Cuando se crea un turno nuevo y hay un operador libre (ACTIVA, sin turno en
     * curso)
     * asignado a esa cola, se lo asigna automáticamente, repartiendo de forma
     * equitativa
     * entre los operadores libres según cuál lleva más tiempo sin recibir un turno.
     *
     * Es "best effort": si dos turnos se crean casi simultáneamente y compiten por
     * el mismo
     * candidato, uno de los dos fallará su intento (condición de carrera) y ese
     * turno se
     * queda pendiente en cola normalmente, sin romper nada.
     *
     * Tolerante a nulls/errores en cada paso: nunca debe lanzar una excepción hacia
     * el
     * llamador, porque esto nunca debe hacer fallar la creación del turno que ya se
     * completó.
     */
    public void intentarAsignarTurnoNuevo(Turno turnoCreado) {
        try {
            if (turnoCreado == null) {
                return;
            }

            Configuracion cfg = configuracionQueryInputPort.buscarPorNombre(turnoCreado.getIdSucursal(),
                    NOMBRE_CONFIGURACION);
            if (cfg == null || cfg.getParametro() == null || cfg.getParametro() != 1) {
                return;
            }

            List<DetalleColaxPuesto> asignaciones = detalleColaxPuestoQueryInputPort.buscarPorCola(
                    turnoCreado.getIdCola(), turnoCreado.getIdDetalle(), turnoCreado.getIdSucursal());
            if (asignaciones == null || asignaciones.isEmpty()) {
                return;
            }

            Set<String> puestosVistos = new HashSet<>();
            List<CandidatoAsignacion> candidatos = new ArrayList<>();

            for (DetalleColaxPuesto asignacion : asignaciones) {
                Long idPuesto = asignacion.getIdPuesto();
                Long idSucursalPuesto = asignacion.getIdSucursalPuesto();
                if (idPuesto == null || idSucursalPuesto == null) {
                    continue;
                }
                String claveDedupe = idPuesto + ":" + idSucursalPuesto;
                if (!puestosVistos.add(claveDedupe)) {
                    continue;
                }

                List<Usuario> usuarios = usuarioQueryInputPort.buscarPorPuesto(idPuesto, idSucursalPuesto);
                if (usuarios == null) {
                    continue;
                }

                for (Usuario usuario : usuarios) {
                    Long idUsuario = usuario.getIdentificador();
                    if (idUsuario == null) {
                        continue;
                    }

                    if (turnoQueryInputPort.existeTurnoLlamadoPorUsuario(idUsuario, idSucursalPuesto,
                            LocalDate.now())) {
                        continue;
                    }

                    EstadoOperador vigente = estadoOperadorQueryInputPort.buscarVigente(idUsuario, idSucursalPuesto);
                    if (vigente == null || vigente.getIdEstadoOperador() == null
                            || !vigente.getIdEstadoOperador().equals(DetalleEstadoOperador.ACTIVA.getValor())) {
                        continue;
                    }

                    if (!turnoWebSocket.tieneSesionActiva(idUsuario)) {
                        continue;
                    }

                    LocalDateTime ultimaFecha = turnoQueryInputPort.obtenerUltimaFechaLlamadaPorUsuario(idUsuario,
                            idSucursalPuesto);
                    candidatos.add(new CandidatoAsignacion(idUsuario, idPuesto, idSucursalPuesto, ultimaFecha));
                }
            }

            if (candidatos.isEmpty()) {
                return;
            }

            CandidatoAsignacion elegido = candidatos.stream()
                    .min(Comparator.comparing(CandidatoAsignacion::ultimaFecha,
                            Comparator.nullsFirst(Comparator.naturalOrder())))
                    .orElse(null);
            if (elegido == null) {
                return;
            }

            Turno turno;
            try {
                turno = turnoCommandInputPort.llamar(turnoCreado.getIdSucursal(), turnoCreado.getFechaCreacion(),
                        turnoCreado.getCodigoTurno(), elegido.idPuesto(), elegido.idSucursalPuesto(),
                        elegido.idUsuario());
            } catch (Exception e) {
                // Condición de carrera esperada (otro turno tomó al mismo candidato primero) u
                // otra validación de negocio: el turno se queda pendiente en cola, normal.
                LOG.log(Level.FINE,
                        "No se pudo auto-asignar el turno nuevo (idSucursal={0}, codigoTurno={1}): {2}",
                        new Object[] { turnoCreado.getIdSucursal(), turnoCreado.getCodigoTurno(), e.getMessage() });
                return;
            }

            TurnoResponseDTO responseDTO = turnoInputMapper.toResponse(turno);
            turnoWebSocket.enviarTurno(wsPayload("TURNO_LLAMADO", turnoCreado.getIdSucursal(), responseDTO));
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error inesperado intentando auto-asignar el turno nuevo", e);
        }
    }
}
