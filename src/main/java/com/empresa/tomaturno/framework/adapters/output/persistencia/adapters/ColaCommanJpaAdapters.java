package com.coop.tomaturno.framework.adapters.output.persistencia.adapters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.coop.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.coop.tomaturno.cola.dominio.entity.Cola;
import com.coop.tomaturno.cola.dominio.entity.Detalle;
import com.coop.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.coop.tomaturno.framework.adapters.output.mapper.ColaOutputMapper;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.ColaDetalleRepository;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.ColaJpaRespository;
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
        cola.setIdentificador(nextId);
        ColaJpaEntity entity = colaOutputMapper.toColaJpaEntity(cola);
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
        detalle.setCorrelativo(nextId);

        DetalleColaJpaEntity detalleEntity = colaOutputMapper.toDetalleJpaEntity(
                idCola, idSucursal.intValue(), detalle);
        colaDetalleRepository.persist(detalleEntity);

        List<DetalleColaJpaEntity> todosLosDetalles = colaDetalleRepository
                .find("id.idCola = ?1 and id.idSucursal = ?2", idCola, idSucursal.intValue())
                .list();

        Cola dominio = colaOutputMapper.toDomain(cola);
        dominio.setDetalles(todosLosDetalles.stream()
                .map(colaOutputMapper::toDomainDetalle)
                .toList());
        return dominio;
    }

    @Override
    public Cola replicarCola(Cola colaOrigen, Long idSucursalDestino) {
        Long nextIdCola = colaJpaRepository.obtenerSiguienteId(idSucursalDestino);

        // Construir cola destino limpia — auditoria propia, sucursal destino
        Cola colaDestino = new Cola();
        colaDestino.setIdentificador(nextIdCola);
        colaDestino.setNombre(colaOrigen.getNombre());
        colaDestino.setCodigo(colaOrigen.getCodigo());
        colaDestino.setPrioridad(colaOrigen.getPrioridad());
        colaDestino.setEstado(colaOrigen.getEstado());
        colaDestino.crearSucursal(idSucursalDestino, null);
        colaDestino.auditoriaCreacion("bmarroquin", LocalDateTime.now());

        ColaJpaEntity colaEntity = colaOutputMapper.toColaJpaEntity(colaDestino);
        colaJpaRepository.persist(colaEntity);

        // Replicar detalles uno a uno
        List<Detalle> detallesOrigen = colaOrigen.getDetalles();
        List<DetalleColaJpaEntity> detallesCreados = new ArrayList<>();

        if (detallesOrigen != null) {
            for (Detalle detalleOrigen : detallesOrigen) {
                Long nextIdDetalle = colaDetalleRepository.obtenerSiguienteIdDetalle(
                        nextIdCola, idSucursalDestino.intValue());

                Detalle detalleNuevo = new Detalle();
                detalleNuevo.setCorrelativo(nextIdDetalle);
                detalleNuevo.setNombre(detalleOrigen.getNombre());
                detalleNuevo.setCodigo(detalleOrigen.getCodigo());
                detalleNuevo.setEstado(detalleOrigen.getEstado());
                detalleNuevo.auditoriaCreacion("bmarroquin", LocalDateTime.now());

                DetalleColaJpaEntity detalleEntity = colaOutputMapper.toDetalleJpaEntity(
                        nextIdCola, idSucursalDestino.intValue(), detalleNuevo);
                colaDetalleRepository.persist(detalleEntity);
                detallesCreados.add(detalleEntity);
            }
        }

        Cola resultado = colaOutputMapper.toDomain(colaEntity);
        resultado.setDetalles(detallesCreados.stream()
                .map(colaOutputMapper::toDomainDetalle)
                .toList());
        return resultado;
    }
}
