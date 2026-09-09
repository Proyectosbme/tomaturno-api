package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.estadooperador.application.command.port.output.EstadoOperadorCommandRepository;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.exceptions.EstadoOperadorNotFoundException;
import com.empresa.tomaturno.framework.adapters.output.mapper.EstadoOperadorOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioEstadoOperadorJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioEstadoOperadorJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstadoOperadorCommandJpaAdapters implements EstadoOperadorCommandRepository {

    private final UsuarioEstadoOperadorJpaRepository repository;
    private final EstadoOperadorOutputMapper mapper;

    public EstadoOperadorCommandJpaAdapters(UsuarioEstadoOperadorJpaRepository repository,
                                        EstadoOperadorOutputMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EstadoOperador guardar(EstadoOperador estadoOperador) {
        UsuarioEstadoOperadorJpaEntity entity = mapper.toJpaEntity(estadoOperador);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public EstadoOperador actualizar(EstadoOperador estadoOperador) {
        UsuarioEstadoOperadorJpaEntity entity = repository.findById(estadoOperador.getId());
        if (entity == null) {
            throw new EstadoOperadorNotFoundException(
                    "No se encontró el registro de estado del operador con id " + estadoOperador.getId());
        }
        mapper.updateEntityFromDomain(estadoOperador, entity);
        return mapper.toDomain(entity);
    }
}
