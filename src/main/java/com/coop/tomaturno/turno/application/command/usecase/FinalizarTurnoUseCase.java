package com.coop.tomaturno.turno.application.command.usecase;

import java.time.LocalDateTime;

import com.coop.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.coop.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.coop.tomaturno.turno.dominio.entity.Turno;
import com.coop.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;

public class FinalizarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;

    public FinalizarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        Turno turno = turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (turno == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno + " sucursal=" + idSucursal);
        }
        turno.validarTransicionFinalizar();
        turno.finalizar();
        return turnoCommandRepository.actualizar(turno);
    }
}
