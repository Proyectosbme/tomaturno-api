package com.coop.tomaturno.framework.adapters.output.persistencia.adapters;

import com.coop.tomaturno.framework.adapters.output.mapper.SucursalOutputMapper;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;
import com.coop.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SucursalCommandJpaAdapter implements SucursalCommandRepository {

    private final SucursalJpaRepository sucursalJpaRepository;
    private final SucursalOutputMapper sucursalOutputMapper;

    public SucursalCommandJpaAdapter(SucursalJpaRepository sucursalJpaRepository,
            SucursalOutputMapper sucursalOutputMapper) {
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.sucursalOutputMapper = sucursalOutputMapper;
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        SucursalJpaEntity entity = sucursalOutputMapper.toSucursalJpaEntity(sucursal);
        sucursalJpaRepository.persist(entity);
        return sucursalOutputMapper.toDomain(entity);
    }

    @Override
    public Sucursal modificar(Sucursal sucursal) {
        SucursalJpaEntity existente = sucursalJpaRepository.findById(sucursal.getIdentificador());
        sucursalOutputMapper.updateEntityFromDomain(sucursal, existente);
        return sucursalOutputMapper.toDomain(existente);
    }
}
