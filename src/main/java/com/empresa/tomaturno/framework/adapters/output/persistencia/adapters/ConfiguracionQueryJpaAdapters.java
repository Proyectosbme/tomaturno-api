package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.framework.adapters.output.mapper.ConfiguracionOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ConfiguracionJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;

@ApplicationScoped
public class ConfiguracionQueryJpaAdapters implements ConfiguracionQueryRepository {

    private final ConfiguracionJpaRepository repository;
    private final ConfiguracionOutputMapper mapper;
    private final SucursalJpaRepository sucursalJpaRepository;

    public ConfiguracionQueryJpaAdapters(ConfiguracionJpaRepository repository,
            ConfiguracionOutputMapper mapper,
            SucursalJpaRepository sucursalJpaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.sucursalJpaRepository = sucursalJpaRepository;
    }

    @Override
    public List<Configuracion> buscarPorSucursal(Long idSucursal) {
        List<ConfiguracionJpaEntity> entities = repository.buscarPorSucursal(idSucursal);
        if (entities.isEmpty())
            return List.of();
        SucursalJpaEntity sucursal = sucursalJpaRepository.findById(idSucursal);
        return entities.stream()
                .map(e -> mapper.toDomainConSucursal(e, sucursal))
                .toList();
    }

    @Override
    public Configuracion buscarPorIdYSucursal(Long idConfiguracion, Long idSucursal) {
        ConfiguracionJpaEntity entity = repository.buscarPorIdYSucursal(idConfiguracion, idSucursal);
        if (entity == null)
            return null;
        SucursalJpaEntity sucursal = sucursalJpaRepository.findById(idSucursal);
        return mapper.toDomainConSucursal(entity, sucursal);
    }

    @Override
    public Configuracion buscarPorNombreYSucursal(Long idSucursal, String nombre) {
        ConfiguracionJpaEntity entity = repository.buscarPorNombreYSucursal(idSucursal, nombre);
        return entity != null ? mapper.toDomain(entity) : null;
    }

    @Override
    public Long obtenerSiguienteId(Long idSucursal) {
        return repository.obtenerSiguienteId(idSucursal);
    }
}
