package com.empresa.tomaturno.turno.application.command.port.output;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.turno.dominio.entity.Turno;

/**
 * Lecturas que el lado command necesita (cargar el turno a mutar, generar código/correlativo,
 * validar transiciones) sin depender del TurnoQueryRepository completo, que pertenece al lado query.
 */
public interface TurnoGatewayPort {

    Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno);

    List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha,
            Long idPuesto, Long idSucursalPuesto);

    Long obtenerSiguienteNumero(Long idSucursal, LocalDate fecha, String codigoBase);

    Long obtenerSiguienteId();

    boolean existeTurnoLlamadoPorPuesto(Long idPuesto, Long idSucursal, LocalDate fecha);

    boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, LocalDate fecha);

    String obtenerCodigoBaseTurno(Long idSucursal, Long idCola, Long idDetalle);

    Long obtenerCorreltivoDetalle(Long idSucursal, Long idCola, Long idDetalle);

    /** Resuelve el detalle válido en la cola destino de una reasignación (dominio Cola). */
    Long resolverDetalleParaReasignacion(Long idCola, Long idSucursal, Long idDetalle);

    /** true si la sucursal exige LLAMAR_CON_ACTIVO (dominio Configuracion). */
    boolean debeVerificarTurnoActivo(Long idSucursal);

    /** true si el operador está ACTIVA (dominio EstadoOperador) — false si está cerrado, en descanso,
     *  o nunca se ha activado. No se puede llamar un turno si el operador no está activo. */
    boolean operadorActivo(Long idUsuario, Long idSucursal);
}
