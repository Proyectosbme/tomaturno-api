package com.coop.tomaturno.turno.application.query.usecase;

import java.time.LocalDate;
import java.util.List;

import com.coop.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.coop.tomaturno.turno.dominio.entity.Turno;

public class BuscarTurnosPorFiltrosUseCase {

    private final TurnoQueryRepository turnoQueryRepository;

    public BuscarTurnosPorFiltrosUseCase(TurnoQueryRepository turnoQueryRepository) {
        this.turnoQueryRepository = turnoQueryRepository;
    }

    public List<Turno> ejecutar(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha) {
        return turnoQueryRepository.buscarPorFiltro(idSucursal, idCola, idDetalle, estado, fecha);
    }
}
