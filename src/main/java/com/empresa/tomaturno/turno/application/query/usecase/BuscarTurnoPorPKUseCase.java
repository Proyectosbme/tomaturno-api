package com.coop.tomaturno.turno.application.query.usecase;

import java.time.LocalDateTime;

import com.coop.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.coop.tomaturno.turno.dominio.entity.Turno;
import com.coop.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;

public class BuscarTurnoPorPKUseCase {

    private final TurnoQueryRepository turnoQueryRepository;

    public BuscarTurnoPorPKUseCase(TurnoQueryRepository turnoQueryRepository) {
        this.turnoQueryRepository = turnoQueryRepository;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        Turno turno = turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (turno == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno + " sucursal=" + idSucursal);
        }
        return turno;
    }
}
