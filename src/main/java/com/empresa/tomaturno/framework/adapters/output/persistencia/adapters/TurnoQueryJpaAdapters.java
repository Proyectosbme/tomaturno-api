package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.mapper.TurnoOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.PuestoJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.TurnoJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioJpaRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TurnoQueryJpaAdapters implements TurnoQueryRepository {

    private final TurnoJpaRepository turnoJpaRepository;
    private final TurnoOutputMapper turnoOutputMapper;
    private final PuestoJpaRepository puestoJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public TurnoQueryJpaAdapters(TurnoJpaRepository turnoJpaRepository,
            TurnoOutputMapper turnoOutputMapper,
            PuestoJpaRepository puestoJpaRepository,
            UsuarioJpaRepository usuarioJpaRepository) {
        this.turnoJpaRepository = turnoJpaRepository;
        this.turnoOutputMapper = turnoOutputMapper;
        this.puestoJpaRepository = puestoJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    private void enriquecerNombreLlamada(Turno turno) {
        if (turno.getIdPuesto() != null && turno.getIdSucursalPuesto() != null) {
            PuestoJpaEntity puesto = puestoJpaRepository.buscarPorIdPuestoYSucursal(
                    turno.getIdPuesto(), turno.getIdSucursalPuesto());
            if (puesto != null) {
                String base = (puesto.getNombreLlamada() != null && !puesto.getNombreLlamada().isBlank())
                        ? puesto.getNombreLlamada()
                        : puesto.getNombre();
                // Usar correlativo del usuario que llamó el turno; si no, fallback al ID del puesto
                String sufijo = "";
                if (turno.getIdUsuario() != null) {
                    UsuarioJpaEntity usuario = usuarioJpaRepository.buscarPorIdUsuarioYSucursal(
                            turno.getIdUsuario(), turno.getIdSucursalPuesto());
                    if (usuario != null && usuario.getCorrelativo() != null) {
                        sufijo = " " + usuario.getCorrelativo();
                    }
                }
                if (sufijo.isBlank() && puesto.getId() != null) {
                    sufijo = " " + puesto.getId();
                }
                turno.setNombreLlamada(base != null ? base + sufijo : null);
            }
        }
    }

    @Override
    public Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        TurnoJpaEntity entity = turnoJpaRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (entity == null) return null;
        Turno turno = turnoOutputMapper.toDomain(entity);
        enriquecerNombreLlamada(turno);
        return turno;
    }

    @Override
    public List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle,
            Integer estado, LocalDate fecha) {
        List<TurnoJpaEntity> entities = turnoJpaRepository.buscarPorFiltros(idSucursal, idCola, idDetalle, estado, fecha);
        List<Turno> turnos = entities.stream().map(turnoOutputMapper::toDomain).toList();
        turnos.forEach(this::enriquecerNombreLlamada);
        return turnos;
    }

    @Override
    public Long obtenerSiguienteNumero(Long idSucursal, LocalDate fecha, String codigoBase) {
        return turnoJpaRepository.obtenerSiguienteNumero(idSucursal, fecha, codigoBase);
    }

    @Override
    public Long obtenerSiguienteId() {
        return turnoJpaRepository.obtenerSiguienteId();
    }

    @Override
    public boolean existeTurnoLlamadoPorPuesto(Long idPuesto, Long idSucursal, LocalDate fecha) {
        return turnoJpaRepository.existeTurnoLlamadoPorPuesto(idPuesto, idSucursal, fecha);
    }

    @Override
    public boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, LocalDate fecha) {
        return turnoJpaRepository.existeTurnoLlamadoPorUsuario(idUsuario, idSucursal, fecha);
    }
}
