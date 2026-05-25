package com.empresa.tomaturno.turno.application.command.service;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.input.LlamarTurnoInputPort;
import com.empresa.tomaturno.turno.application.command.usecase.LlamarTurnoUseCase;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

public class LlamarTurnoAdapter implements LlamarTurnoInputPort {

    private final LlamarTurnoUseCase llamarTurnoUseCase;

    public LlamarTurnoAdapter(LlamarTurnoUseCase llamarTurnoUseCase) {
        this.llamarTurnoUseCase = llamarTurnoUseCase;
    }

    @Override
    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        return llamarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno,
                idPuesto, idSucursalPuesto, idUsuario);
    }
}
