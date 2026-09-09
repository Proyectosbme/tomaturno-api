package com.empresa.tomaturno.turno.application.query.port.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.turno.dominio.entity.Turno;

public interface TurnoQueryInputPort {
    Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno);
    List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha,
            Long idPuesto, Long idSucursalPuesto);
    /** Verifica si el usuario ya tiene un turno en estado LLAMADO hoy */
    boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, LocalDate fecha);
    /** MAX(fechaLlamada) histórico del usuario en la sucursal, o null si nunca recibió un turno */
    LocalDateTime obtenerUltimaFechaLlamadaPorUsuario(Long idUsuario, Long idSucursal);
}
