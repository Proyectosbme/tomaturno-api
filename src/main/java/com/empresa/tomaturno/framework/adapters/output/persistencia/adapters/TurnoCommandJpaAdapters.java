package com.coop.tomaturno.framework.adapters.output.persistencia.adapters;

import com.coop.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.coop.tomaturno.turno.dominio.entity.Turno;
import com.coop.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.coop.tomaturno.framework.adapters.output.mapper.TurnoOutputMapper;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.TurnoJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TurnoCommandJpaAdapters implements TurnoCommandRepository {

    private final TurnoJpaRepository turnoJpaRepository;
    private final TurnoOutputMapper turnoOutputMapper;

    public TurnoCommandJpaAdapters(TurnoJpaRepository turnoJpaRepository,
            TurnoOutputMapper turnoOutputMapper) {
        this.turnoJpaRepository = turnoJpaRepository;
        this.turnoOutputMapper = turnoOutputMapper;
    }

    @Override
    public Turno save(Turno turno) {
        TurnoJpaEntity entity = turnoOutputMapper.toJpaEntity(turno);
        turnoJpaRepository.persist(entity);
        return turnoOutputMapper.toDomain(entity);
    }

    @Override
    public Turno actualizar(Turno turno) {
        TurnoJpaEntity existente = turnoJpaRepository.buscarPorPK(
                turno.getIdSucursal(), turno.getFechaCreacion(), turno.getCodigoTurno());
        if (existente == null) {
            throw new NotFoundException("Turno no encontrado: " + turno.getCodigoTurno());
        }
        turnoOutputMapper.updateEntityFromDomain(turno, existente);
        return turnoOutputMapper.toDomain(existente);
    }

    @Override
    public Turno reasignar(Turno turnoOriginal, Turno turnoNuevo) {
        // Actualizar el original a estado TRASLADO
        TurnoJpaEntity existente = turnoJpaRepository.buscarPorPK(
                turnoOriginal.getIdSucursal(), turnoOriginal.getFechaCreacion(), turnoOriginal.getCodigoTurno());
        if (existente == null) {
            throw new NotFoundException("Turno original no encontrado: " + turnoOriginal.getCodigoTurno());
        }
        turnoOutputMapper.updateEntityFromDomain(turnoOriginal, existente);

        // Persistir el nuevo turno
        TurnoJpaEntity nuevoEntity = turnoOutputMapper.toJpaEntity(turnoNuevo);
        turnoJpaRepository.persist(nuevoEntity);

        return turnoOutputMapper.toDomain(nuevoEntity);
    }
}
