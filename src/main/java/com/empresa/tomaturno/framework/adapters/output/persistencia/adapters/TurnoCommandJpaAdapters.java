package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.empresa.tomaturno.framework.adapters.output.mapper.TurnoOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.PuestoJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.TurnoJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioJpaRepository;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TurnoCommandJpaAdapters implements TurnoCommandRepository {

    private final TurnoJpaRepository turnoJpaRepository;
    private final TurnoOutputMapper turnoOutputMapper;
    private final PuestoJpaRepository puestoJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public TurnoCommandJpaAdapters(TurnoJpaRepository turnoJpaRepository,
            TurnoOutputMapper turnoOutputMapper,
            PuestoJpaRepository puestoJpaRepository,
            UsuarioJpaRepository usuarioJpaRepository) {
        this.turnoJpaRepository = turnoJpaRepository;
        this.turnoOutputMapper = turnoOutputMapper;
        this.puestoJpaRepository = puestoJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    private void enriquecerNombreLlamada(Turno turno) {
        if (turno.getIdPuesto() == null || turno.getIdSucursalPuesto() == null)
            return;
        String base = puestoJpaRepository.obtenerNombreLlamada(turno.getIdPuesto(), turno.getIdSucursalPuesto());
        if (base == null)
            return;
        String correlativo = turno.getIdUsuario() != null
                ? usuarioJpaRepository.obtenerCorrelativo(turno.getIdUsuario(), turno.getIdSucursalPuesto())
                : null;
        String sufijo = correlativo != null ? " " + correlativo : " " + turno.getIdPuesto();
        turno.enriquecerNombreLlamada(base + sufijo);
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
        Turno resultado = turnoOutputMapper.toDomain(existente);
        enriquecerNombreLlamada(resultado);
        return resultado;
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
