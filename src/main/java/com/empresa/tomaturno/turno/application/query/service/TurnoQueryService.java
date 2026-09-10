package com.empresa.tomaturno.turno.application.query.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.turno.application.query.dto.TurnoHoyDTO;
import com.empresa.tomaturno.turno.application.query.port.input.TurnoQueryInputPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.application.query.usecase.BuscarTurnoPorPKUseCase;
import com.empresa.tomaturno.turno.application.query.usecase.BuscarTurnosPorFiltrosUseCase;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

public class TurnoQueryService implements TurnoQueryInputPort {

    private final BuscarTurnoPorPKUseCase buscarTurnoPorPKUseCase;
    private final BuscarTurnosPorFiltrosUseCase buscarTurnosPorFiltrosUseCase;
    private final TurnoQueryRepository turnoQueryRepository;

    public TurnoQueryService(TurnoQueryRepository turnoQueryRepository) {
        this.buscarTurnoPorPKUseCase = new BuscarTurnoPorPKUseCase(turnoQueryRepository);
        this.buscarTurnosPorFiltrosUseCase = new BuscarTurnosPorFiltrosUseCase(turnoQueryRepository);
        this.turnoQueryRepository = turnoQueryRepository;
    }

    @Override
    public Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return buscarTurnoPorPKUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno);
    }

    @Override
    public List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha,
            Long idPuesto, Long idSucursalPuesto) {
        return buscarTurnosPorFiltrosUseCase.ejecutar(idSucursal, idCola, idDetalle, estado, fecha,
                idPuesto, idSucursalPuesto);
    }

    @Override
    public boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, LocalDate fecha) {
        return turnoQueryRepository.existeTurnoLlamadoPorUsuario(idUsuario, idSucursal, fecha);
    }

    @Override
    public LocalDateTime obtenerUltimaFechaLlamadaPorUsuario(Long idUsuario, Long idSucursal) {
        return turnoQueryRepository.obtenerUltimaFechaLlamadaPorUsuario(idUsuario, idSucursal);
    }

    @Override
    public List<TurnoHoyDTO> buscarTurnosHoy(Long idSucursal) {
        return turnoQueryRepository.buscarTurnosHoy(idSucursal);
    }

    @Override
    public List<TurnoHoyDTO> buscarTurnosHoyPorUsuario(Long idUsuario, Long idSucursal) {
        return turnoQueryRepository.buscarTurnosHoyPorUsuario(idUsuario, idSucursal);
    }
}
