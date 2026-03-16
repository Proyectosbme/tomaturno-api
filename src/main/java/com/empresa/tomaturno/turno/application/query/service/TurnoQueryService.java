package com.coop.tomaturno.turno.application.query.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.coop.tomaturno.turno.application.query.port.input.TurnoQueryInputPort;
import com.coop.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.coop.tomaturno.turno.application.query.usecase.BuscarTurnoPorPKUseCase;
import com.coop.tomaturno.turno.application.query.usecase.BuscarTurnosPorFiltrosUseCase;
import com.coop.tomaturno.turno.dominio.entity.Turno;

public class TurnoQueryService implements TurnoQueryInputPort {

    private final BuscarTurnoPorPKUseCase buscarTurnoPorPKUseCase;
    private final BuscarTurnosPorFiltrosUseCase buscarTurnosPorFiltrosUseCase;

    public TurnoQueryService(TurnoQueryRepository turnoQueryRepository) {
        this.buscarTurnoPorPKUseCase = new BuscarTurnoPorPKUseCase(turnoQueryRepository);
        this.buscarTurnosPorFiltrosUseCase = new BuscarTurnosPorFiltrosUseCase(turnoQueryRepository);
    }

    @Override
    public Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return buscarTurnoPorPKUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno);
    }

    @Override
    public List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha) {
        return buscarTurnosPorFiltrosUseCase.ejecutar(idSucursal, idCola, idDetalle, estado, fecha);
    }
}
