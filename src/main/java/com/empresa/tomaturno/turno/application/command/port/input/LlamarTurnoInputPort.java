package com.empresa.tomaturno.turno.application.command.port.input;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.dominio.entity.Turno;

public interface LlamarTurnoInputPort {
    Turno llamar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idPuesto, Long idSucursalPuesto, Long idUsuario);
}
