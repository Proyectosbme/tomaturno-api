package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Sucursal;
import com.empresa.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.empresa.tomaturno.framework.adapters.output.mapper.ColaOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ColaDetalleRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ColaJpaRespository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ColaCommanJpaAdapters implements ColaCommandRepository {

    private final ColaJpaRespository colaJpaRepository;
    private final ColaOutputMapper colaOutputMapper;
    private final ColaDetalleRepository colaDetalleRepository;

    public ColaCommanJpaAdapters(ColaJpaRespository colaJpaRepository,
            ColaOutputMapper colaOutputMapper,
            ColaDetalleRepository colaDetalleRepository) {
        this.colaJpaRepository = colaJpaRepository;
        this.colaOutputMapper = colaOutputMapper;
        this.colaDetalleRepository = colaDetalleRepository;
    }

    @Override
    public Cola save(Cola cola) {
        Long nextId = colaJpaRepository.obtenerSiguienteId(cola.getSucursal().getIdentificador());
        ColaJpaEntity entity = colaOutputMapper.toColaJpaEntity(cola);
        entity.getIdpk().setId(nextId);
        colaJpaRepository.persist(entity);
        return colaOutputMapper.toDomain(entity);
    }

    @Override
    public Cola modificar(Cola cola) {
        ColaJpaEntity existente = colaJpaRepository
                .buscarPorIdColaYSucursal(cola.getIdentificador(), cola.getSucursal().getIdentificador());
        if (existente == null) {
            throw new NotFoundException("Cola no encontrada con idCola: " + cola.getIdentificador()
                    + " e idSucursal: " + cola.getSucursal().getIdentificador());
        }
        colaOutputMapper.updateEntityFromDomain(cola, existente);
        return colaOutputMapper.toDomain(existente);
    }

    @Override
    public Cola guardarDetalle(Long idCola, Long idSucursal, Detalle detalle) {
        ColaJpaEntity cola = colaJpaRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new NotFoundException("Cola no encontrada con idCola: " + idCola
                    + " e idSucursal: " + idSucursal);
        }
        Long nextId = colaDetalleRepository.obtenerSiguienteIdDetalle(idCola, idSucursal.intValue());
        DetalleColaJpaEntity detalleEntity = colaOutputMapper.toDetalleJpaEntity(
                idCola, idSucursal.intValue(), detalle);
        detalleEntity.getId().setIdDetalle(nextId);
        colaDetalleRepository.persist(detalleEntity);

        List<DetalleColaJpaEntity> todosLosDetalles = colaDetalleRepository
                .buscarPorFiltro(idSucursal, idCola, null);
        return colaOutputMapper.toDomainConDetalles(cola, todosLosDetalles);
    }

    @Override
    public Cola replicarCola(Cola colaOrigen, Long idSucursalDestino, String usuario) {
        Long nextIdCola = colaJpaRepository.obtenerSiguienteId(idSucursalDestino);

        Cola colaDestino = Cola.builder()
                .nombre(colaOrigen.getNombre())
                .codigo(colaOrigen.getCodigo())
                .prioridad(colaOrigen.getPrioridad())
                .estado(colaOrigen.getEstado())
                .sucursal(new Sucursal(idSucursalDestino, null))
                .inicializar();
        colaDestino.crear(usuario);

        ColaJpaEntity colaEntity = colaOutputMapper.toColaJpaEntity(colaDestino);
        colaEntity.getIdpk().setId(nextIdCola);
        colaJpaRepository.persist(colaEntity);

        List<DetalleColaJpaEntity> detallesCreados = new ArrayList<>();
        List<Detalle> detallesOrigen = colaOrigen.getDetalles();
        if (detallesOrigen != null) {
            for (Detalle detalleOrigen : detallesOrigen) {
                Long nextIdDetalle = colaDetalleRepository.obtenerSiguienteIdDetalle(
                        nextIdCola, idSucursalDestino.intValue());

                Detalle detalleNuevo = Detalle.inicializar(
                        detalleOrigen.getNombre(),
                        detalleOrigen.getCodigo(),
                        detalleOrigen.getEstado());
                detalleNuevo.crear(usuario);

                DetalleColaJpaEntity detalleEntity = colaOutputMapper.toDetalleJpaEntity(
                        nextIdCola, idSucursalDestino.intValue(), detalleNuevo);
                detalleEntity.getId().setIdDetalle(nextIdDetalle);
                colaDetalleRepository.persist(detalleEntity);
                detallesCreados.add(detalleEntity);
            }
        }

        return colaOutputMapper.toDomainConDetalles(colaEntity, detallesCreados);
    }

    @Override
    public Cola modificarDetalle(Long idCola, Long idSucursal, Detalle detalleActualizado) {
        DetalleColaPK pk = new DetalleColaPK(idCola, idSucursal.intValue(), detalleActualizado.getCorrelativo());
        DetalleColaJpaEntity existente = colaDetalleRepository.findById(pk);
        if (existente == null) {
            throw new NotFoundException("Detalle no encontrado con idCola: " + idCola
                    + ", idSucursal: " + idSucursal + " y idDetalle: " + detalleActualizado.getCorrelativo());
        }
        colaOutputMapper.updateDetalleEntityFromDomain(detalleActualizado, existente);

        ColaJpaEntity colaEntity = colaJpaRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        List<DetalleColaJpaEntity> todosLosDetalles = colaDetalleRepository
                .buscarPorFiltro(idSucursal, idCola, null);
        return colaOutputMapper.toDomainConDetalles(colaEntity, todosLosDetalles);
    }
}
