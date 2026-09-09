package com.empresa.tomaturno.turno.application.query.dto;

import java.time.LocalDateTime;

/**
 * Fila de la vista tomaturno.vwturnoshoy: un turno creado hoy, ya resuelto con los
 * nombres de sucursal/usuario/puesto/cola/detalle/estado. Es un modelo de solo lectura
 * (para reportes y monitoreo), no un agregado de negocio — por eso es un record plano,
 * sin las validaciones/transiciones que tiene Turno.
 */
public record TurnoHoyDTO(
        Long id,
        String codigoTurno,
        Long idSucursalTicket,
        String sucursalTicket,
        Long idUsuario,
        String nombreCompleto,
        String codigoUsuario,
        Long idPuesto,
        Long idPuestoSucursal,
        String puesto,
        Long idCola,
        String cola,
        Long idDetalle,
        String detalle,
        Integer tipoCasoEspecial,
        String casoEspecial,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaLlamada,
        LocalDateTime fechaFinalizacion,
        Long idCatalogoEstado,
        Long idCatalogoEstadoDetalle,
        String estadoTurno,
        Long idTurnoRelacionado) {
}
