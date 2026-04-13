package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;

public class MarcarSinAtenderUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;

    public MarcarSinAtenderUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        Turno turno = turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (turno == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno + " sucursal=" + idSucursal);
        }
        turno.sinAtender();
        return turnoCommandRepository.actualizar(turno);
    }
}
