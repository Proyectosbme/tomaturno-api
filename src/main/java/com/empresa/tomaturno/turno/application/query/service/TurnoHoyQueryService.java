package com.empresa.tomaturno.turno.application.query.service;

import java.util.List;

import com.empresa.tomaturno.turno.application.query.dto.TurnoHoyDTO;
import com.empresa.tomaturno.turno.application.query.port.input.TurnoHoyQueryInputPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoHoyQueryRepository;

public class TurnoHoyQueryService implements TurnoHoyQueryInputPort {

    private final TurnoHoyQueryRepository queryRepository;

    public TurnoHoyQueryService(TurnoHoyQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public List<TurnoHoyDTO> buscarTodos(Long idSucursal) {
        return queryRepository.buscarTodos(idSucursal);
    }

    @Override
    public List<TurnoHoyDTO> buscarPorUsuario(Long idUsuario, Long idSucursal) {
        return queryRepository.buscarPorUsuario(idUsuario, idSucursal);
    }
}
