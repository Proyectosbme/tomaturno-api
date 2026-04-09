package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.empresa.tomaturno.framework.adapters.output.mapper.PuestoOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.PuestoJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;
import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PuestoQueryJpaAdapters implements PuestoQueryRepository {

    private final PuestoJpaRepository puestoJpaRepository;
    private final PuestoOutputMapper puestoOutputMapper;
    private final SucursalJpaRepository sucursalJpaRepository;

    public PuestoQueryJpaAdapters(PuestoJpaRepository puestoJpaRepository,
            PuestoOutputMapper puestoOutputMapper,
            SucursalJpaRepository sucursalJpaRepository) {
        this.puestoJpaRepository = puestoJpaRepository;
        this.puestoOutputMapper = puestoOutputMapper;
        this.sucursalJpaRepository = sucursalJpaRepository;
    }

    @Override
    public Puesto buscarPorIdPuestoYSucursal(Long idPuesto, Long idSucursal) {
        PuestoJpaEntity entity = puestoJpaRepository.buscarPorIdPuestoYSucursal(idPuesto, idSucursal);
        if (entity == null) return null;
        SucursalJpaEntity sucursal = sucursalJpaRepository.findById(idSucursal);
        return puestoOutputMapper.toDomainConSucursal(entity, sucursal);
    }

    @Override
    public List<Puesto> buscarPorFiltro(Long idSucursal, String nombre) {
        List<PuestoJpaEntity> entities = puestoJpaRepository.buscarPorFiltros(idSucursal, nombre);
        if (entities.isEmpty()) return List.of();

        List<Long> idsSucursales = entities.stream()
                .map(e -> e.getIdpk().getIdSucursal())
                .distinct().toList();
        Map<Long, SucursalJpaEntity> sucursalesMap = sucursalJpaRepository
                .find("id in ?1", idsSucursales).stream()
                .collect(Collectors.toMap(SucursalJpaEntity::getId, s -> s));

        return entities.stream().map(entity -> {
            SucursalJpaEntity sucursal = sucursalesMap.get(entity.getIdpk().getIdSucursal());
            return puestoOutputMapper.toDomainConSucursal(entity, sucursal);
        }).toList();
    }

    @Override
    public boolean existeNombreEnSucursal(Long idSucursal, String nombre) {
        return puestoJpaRepository.existeNombreEnSucursal(idSucursal, nombre);
    }
}
