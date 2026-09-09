package com.empresa.tomaturno.turno.application.query.port.output;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.turno.dominio.entity.Turno;

public interface TurnoQueryRepository {
    Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno);
    List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha,
            Long idPuesto, Long idSucursalPuesto);
    /** Siguiente número correlativo para sucursal+fecha+codigoBase (reinicia cada día) */
    Long obtenerSiguienteNumero(Long idSucursal, LocalDate fecha, String codigoBase);
    /** Siguiente id global (para idTurnoRelacionado) */
    Long obtenerSiguienteId();
    /** Verifica si el puesto ya tiene un turno en estado LLAMADO (2) hoy */
    boolean existeTurnoLlamadoPorPuesto(Long idPuesto, Long idSucursal, java.time.LocalDate fecha);
    /** Verifica si el usuario ya tiene un turno en estado LLAMADO (2) hoy */
    boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, java.time.LocalDate fecha);
    /** MAX(fechaLlamada) histórico del usuario en la sucursal (sin filtrar por fecha ni estado), o null si nunca recibió un turno */
    LocalDateTime obtenerUltimaFechaLlamadaPorUsuario(Long idUsuario, Long idSucursal);

    String obtenerCodigoBaseTurno( Long idSucursal,Long idCola, Long idDetalle);
    Long obtenerCorreltivoDetalle (Long idSucursal, Long idCola,Long idDetalle );
}
