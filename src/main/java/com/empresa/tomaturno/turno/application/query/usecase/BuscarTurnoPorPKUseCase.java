package com.empresa.tomaturno.turno.application.query.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;

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
