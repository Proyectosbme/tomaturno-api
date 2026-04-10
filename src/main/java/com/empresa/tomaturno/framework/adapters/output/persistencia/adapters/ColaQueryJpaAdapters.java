package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.framework.adapters.output.mapper.ColaOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ColaDetalleRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ColaJpaRespository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ColaQueryJpaAdapters implements ColaQueryRepository {

    private final ColaJpaRespository colaJpaRepository;
    private final ColaOutputMapper colaOutputMapper;
    private final SucursalJpaRepository sucursalJpaRepository;
    private final ColaDetalleRepository colaDetalleRepository;

    public ColaQueryJpaAdapters(ColaJpaRespository colaJpaRepository,
            ColaOutputMapper colaOutputMapper,
            SucursalJpaRepository sucursalJpaRepository,
            ColaDetalleRepository colaDetalleRepository) {
        this.colaJpaRepository = colaJpaRepository;
        this.colaOutputMapper = colaOutputMapper;
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.colaDetalleRepository = colaDetalleRepository;
    }

    @Override
    public Cola buscarPorIdColaYSucursal(Long idCola, Long idSucursal) {
        ColaJpaEntity entity = colaJpaRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (entity == null) return null;
        SucursalJpaEntity sucursal = sucursalJpaRepository.findById(idSucursal);
        return colaOutputMapper.toDomainConSucursal(entity, sucursal);
    }

    @Override
    public List<Cola> buscarPorFiltro(Long id, Long idSucursal, String nombre) {
        List<ColaJpaEntity> entities = colaJpaRepository.buscarPorfiltros(id, idSucursal, nombre);
        if (entities.isEmpty()) return List.of();

        List<Long> idsSucursales = entities.stream()
                .map(e -> e.getIdpk().getIdSucursal())
                .distinct().toList();
        Map<Long, SucursalJpaEntity> sucursalesMap = sucursalJpaRepository
                .find("id in ?1", idsSucursales).stream()
                .collect(Collectors.toMap(SucursalJpaEntity::getId, s -> s));

        return entities.stream().map(entity -> {
            SucursalJpaEntity sucursal = sucursalesMap.get(entity.getIdpk().getIdSucursal());
            List<DetalleColaJpaEntity> detalles = colaDetalleRepository.buscarPorFiltro(
                    entity.getIdpk().getIdSucursal(), entity.getIdpk().getId(), null);
            return colaOutputMapper.toDomainCompleto(entity, sucursal, detalles);
        }).toList();
    }

    @Override
    public Cola buscarConDetallesPorIdYSucursal(Long idCola, Long idSucursal) {
        ColaJpaEntity entity = colaJpaRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (entity == null) return null;
        SucursalJpaEntity sucursal = sucursalJpaRepository.findById(idSucursal);
        List<DetalleColaJpaEntity> detalles = colaDetalleRepository.buscarPorFiltro(idSucursal, idCola, null);
        return colaOutputMapper.toDomainCompleto(entity, sucursal, detalles);
    }

    @Override
    public List<Cola> buscarConDetallesPorSucursal(Long idSucursal) {
        List<ColaJpaEntity> entities = colaJpaRepository.buscarPorSucursal(idSucursal);
        if (entities.isEmpty()) return List.of();
        SucursalJpaEntity sucursal = sucursalJpaRepository.findById(idSucursal);
        return entities.stream().map(entity -> {
            List<DetalleColaJpaEntity> detalles = colaDetalleRepository.buscarPorFiltro(
                    idSucursal, entity.getIdpk().getId(), null);
            return colaOutputMapper.toDomainCompleto(entity, sucursal, detalles);
        }).toList();
    }

    @Override
    public boolean existeNombreEnSucursal(Long idSucursal, String nombre) {
        return colaJpaRepository.existeNombreEnSucursal(idSucursal, nombre);
    }

    @Override
    public boolean existeNombreDetalleEnCola(Long idCola, Long idSucursal, String nombreDetalle) {
        return colaDetalleRepository.existeNombreEnCola(idCola, idSucursal, nombreDetalle);
    }

    @Override
    public Detalle obtenerDetalle(Long idCola, Long idSucursal, Long idDetalle) {
        DetalleColaPK pk = new DetalleColaPK(idCola, idSucursal.intValue(), idDetalle);
        DetalleColaJpaEntity entity = colaDetalleRepository.findById(pk);
        if (entity == null) return null;
        return colaOutputMapper.toDomainDetalle(entity);
    }

    @Override
    public List<Cola> buscarColasQueTienenDetalles(Long idSucursal) {
        List<ColaJpaEntity> entities = colaJpaRepository.buscarPorSucursal(idSucursal);
        if (entities.isEmpty()) return List.of();
        SucursalJpaEntity sucursal = sucursalJpaRepository.findById(idSucursal);
        return entities.stream()
                .flatMap(entity -> {
                    List<DetalleColaJpaEntity> detalles = colaDetalleRepository.buscarPorFiltro(
                            idSucursal, entity.getIdpk().getId(), null);
                    return detalles != null && !detalles.isEmpty()
                            ? Optional.of(colaOutputMapper.toDomainCompleto(entity, sucursal, detalles)).stream()
                            : Stream.empty();
                })
                .toList();
    }
}
