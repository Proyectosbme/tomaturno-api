package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.mapper.SucursalOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;
import com.empresa.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SucursalQueryJpaAdapter implements SucursalQueryRepository {

    private final SucursalJpaRepository sucursalJpaRepository;
    private final SucursalOutputMapper sucursalOutputMapper;

    public SucursalQueryJpaAdapter(SucursalJpaRepository sucursalJpaRepository,
            SucursalOutputMapper sucursalOutputMapper) {
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.sucursalOutputMapper = sucursalOutputMapper;
    }

    @Override
    public List<Sucursal> listarTodas() {
        return sucursalJpaRepository.listAll()
                .stream()
                .map(sucursalOutputMapper::toDomain)
                .toList();
    }

    @Override
    public Sucursal buscarPorId(Long id) {
        SucursalJpaEntity entity = sucursalJpaRepository.findById(id);
        return entity != null ? sucursalOutputMapper.toDomain(entity) : null;
    }

    @Override
    public List<Sucursal> buscarPorNombre(String nombre) {
        return sucursalJpaRepository.buscarPorNombre(nombre)
                .stream()
                .map(sucursalOutputMapper::toDomain)
                .toList();
    }
}
