package com.coop.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.coop.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.coop.tomaturno.cola.dominio.entity.Cola;
import com.coop.tomaturno.cola.dominio.entity.Detalle;
import com.coop.tomaturno.framework.adapters.output.mapper.ColaOutputMapper;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.ColaDetalleRepository;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.ColaJpaRespository;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;

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
        ColaJpaEntity colaJpaEntity = colaJpaRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (colaJpaEntity == null) return null;
        Cola cola = colaOutputMapper.toDomain(colaJpaEntity);
        enriquecerConSucursal(cola);
        return cola;
    }

    @Override
    public List<Cola> buscarPorFiltro(Long id, Long idSucursal, String nombre) {
        List<ColaJpaEntity> lstColaJpaEntity = colaJpaRepository.buscarPorfiltros(id, idSucursal, nombre);
        if (lstColaJpaEntity.isEmpty()) return List.of();

        List<Cola> lstCola = lstColaJpaEntity.stream()
                .map(colaOutputMapper::toDomain)
                .toList();

        List<Long> idsSucursales = lstCola.stream()
                .map(c -> c.getSucursal().getIdentificador())
                .distinct().toList();

        Map<Long, SucursalJpaEntity> sucursalesMap = sucursalJpaRepository
                .find("id in ?1", idsSucursales).stream()
                .collect(Collectors.toMap(SucursalJpaEntity::getId, s -> s));

        lstCola.forEach(cola -> {
            SucursalJpaEntity sucursal = sucursalesMap.get(cola.getSucursal().getIdentificador());
            if (sucursal != null)
                cola.crearSucursal(sucursal.getId(), sucursal.getNombre());
        });

        return lstCola;
    }

    @Override
    public Cola buscarConDetallesPorIdYSucursal(Long idCola, Long idSucursal) {
        ColaJpaEntity entity = colaJpaRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (entity == null) return null;
        Cola cola = colaOutputMapper.toDomain(entity);
        enriquecerConSucursal(cola);
        cargarDetalles(cola);
        return cola;
    }

    @Override
    public List<Cola> buscarConDetallesPorSucursal(Long idSucursal) {
        List<ColaJpaEntity> entities = colaJpaRepository.buscarPorSucursal(idSucursal);
        if (entities.isEmpty()) return List.of();

        SucursalJpaEntity sucursalEntity = sucursalJpaRepository.findById(idSucursal);

        return entities.stream().map(entity -> {
            Cola cola = colaOutputMapper.toDomain(entity);
            if (sucursalEntity != null)
                cola.crearSucursal(sucursalEntity.getId(), sucursalEntity.getNombre());
            cargarDetalles(cola);
            return cola;
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

    // ── privados ──────────────────────────────────────────────────────────────

    private void cargarDetalles(Cola cola) {
        List<DetalleColaJpaEntity> entidades = colaDetalleRepository
                .find("id.idCola = ?1 and id.idSucursal = ?2",
                        cola.getIdentificador(),
                        cola.getSucursal().getIdentificador().intValue())
                .list();

        List<Detalle> detalles = entidades.stream()
                .map(colaOutputMapper::toDomainDetalle)
                .toList();

        cola.setDetalles(detalles);
    }

    private void enriquecerConSucursal(Cola cola) {
        SucursalJpaEntity sucursal = sucursalJpaRepository
                .findById(cola.getSucursal().getIdentificador());
        if (sucursal != null)
            cola.crearSucursal(sucursal.getId(), sucursal.getNombre());
    }
}
