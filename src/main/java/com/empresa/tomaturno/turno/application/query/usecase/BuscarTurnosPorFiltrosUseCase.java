package com.empresa.tomaturno.turno.application.query.usecase;

import java.time.LocalDate;
import java.util.List;

import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

public class BuscarTurnosPorFiltrosUseCase {

    private final TurnoQueryRepository turnoQueryRepository;

    public BuscarTurnosPorFiltrosUseCase(TurnoQueryRepository turnoQueryRepository) {
        this.turnoQueryRepository = turnoQueryRepository;
    }

    public List<Turno> ejecutar(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha,
            Long idPuesto, Long idSucursalPuesto) {
        return turnoQueryRepository.buscarPorFiltro(idSucursal, idCola, idDetalle, estado, fecha,
                idPuesto, idSucursalPuesto);
    }
}
