package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.mapper.TurnoHoyOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.VwTurnosHoyJpaRepository;
import com.empresa.tomaturno.turno.application.query.dto.TurnoHoyDTO;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoHoyQueryRepository;

@ApplicationScoped
public class TurnoHoyQueryJpaAdapters implements TurnoHoyQueryRepository {

    private final VwTurnosHoyJpaRepository repository;
    private final TurnoHoyOutputMapper mapper;

    public TurnoHoyQueryJpaAdapters(VwTurnosHoyJpaRepository repository, TurnoHoyOutputMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TurnoHoyDTO> buscarTodos(Long idSucursal) {
        return repository.buscarTodos(idSucursal).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<TurnoHoyDTO> buscarPorUsuario(Long idUsuario, Long idSucursal) {
        return repository.buscarPorUsuario(idUsuario, idSucursal).stream().map(mapper::toDomain).toList();
    }
}
