package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (entities.isEmpty()) return List.of();

        List<Configuracion> configs = entities.stream()
                .map(mapper::toDomain)
                .toList();

        enriquecerConNombreSucursal(configs);
        return configs;
    }

    @Override
    public Configuracion buscarPorIdYSucursal(Long idConfiguracion, Long idSucursal) {
        ConfiguracionJpaEntity entity = repository.buscarPorIdYSucursal(idConfiguracion, idSucursal);
        if (entity == null) return null;
        Configuracion config = mapper.toDomain(entity);
        enriquecerConNombreSucursal(List.of(config));
        return config;
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

    private void enriquecerConNombreSucursal(List<Configuracion> configs) {
        List<Long> idsSucursales = configs.stream()
                .map(Configuracion::getIdSucursal).distinct().toList();

        Map<Long, String> nombresSucursales = sucursalJpaRepository
                .find("id in ?1", idsSucursales).stream()
                .collect(Collectors.toMap(SucursalJpaEntity::getId, SucursalJpaEntity::getNombre));

        configs.forEach(c -> c.setNombreSucursal(
                nombresSucursales.getOrDefault(c.getIdSucursal(), "")));
    }
}
