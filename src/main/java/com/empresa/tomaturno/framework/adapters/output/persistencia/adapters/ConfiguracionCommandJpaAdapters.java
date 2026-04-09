package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.framework.adapters.output.mapper.ConfiguracionOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ConfiguracionJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfiguracionCommandJpaAdapters implements ConfiguracionCommandRepository {

    private final ConfiguracionJpaRepository repository;
    private final ConfiguracionOutputMapper mapper;

    public ConfiguracionCommandJpaAdapters(ConfiguracionJpaRepository repository,
                                           ConfiguracionOutputMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Configuracion save(Configuracion configuracion) {
        Long nextId = repository.obtenerSiguienteId(configuracion.getIdSucursal());
        ConfiguracionJpaEntity entity = mapper.toJpaEntity(configuracion);
        entity.getIdpk().setIdConfiguracion(nextId);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Configuracion modificar(Configuracion configuracion) {
        ConfiguracionJpaEntity entity = repository.buscarPorIdYSucursal(
                configuracion.getIdConfiguracion(), configuracion.getIdSucursal());
        mapper.updateEntityFromDomain(configuracion, entity);
        return mapper.toDomain(entity);
    }
}
