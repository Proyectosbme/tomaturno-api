package com.empresa.tomaturno.turno.application.command.port.input;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.dominio.entity.Turno;

public interface TurnoCommandInputPort {
    Turno crear(Long idSucursal, Long idCola, Long idDetalle, Long idPersona, Integer tipoCasoEspecial);
    Turno llamar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno, Long idPuesto, Long idSucursalPuesto, Long idUsuario);
    Turno llamarSiguiente(Long idSucursal, Long idPuesto, Long idSucursalPuesto, Long idUsuario);
    Turno reasignar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno, Long idSucursalDestino, Long idColaDestino, Long idDetalleDestino);
    Turno sinAtender(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno);
    Turno finalizar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno);
    Turno rellamar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno, Long idPuesto, Long idSucursalPuesto, Long idUsuario);
}
