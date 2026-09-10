package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import jakarta.enterprise.context.ApplicationScoped;

import com.empresa.tomaturno.estadooperador.application.query.port.output.EstadoOperadorQueryRepository;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.framework.adapters.output.mapper.EstadoOperadorOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioEstadoOperadorJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioEstadoOperadorJpaRepository;

@ApplicationScoped
public class EstadoOperadorQueryJpaAdapters implements EstadoOperadorQueryRepository {

    private final UsuarioEstadoOperadorJpaRepository repository;
    private final EstadoOperadorOutputMapper mapper;

    public EstadoOperadorQueryJpaAdapters(UsuarioEstadoOperadorJpaRepository repository,
                                      EstadoOperadorOutputMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EstadoOperador buscarVigente(Long idUsuario, Long idSucursal) {
        UsuarioEstadoOperadorJpaEntity entity = repository.buscarVigente(idUsuario, idSucursal);
        return mapper.toDomain(entity);
    }
}
