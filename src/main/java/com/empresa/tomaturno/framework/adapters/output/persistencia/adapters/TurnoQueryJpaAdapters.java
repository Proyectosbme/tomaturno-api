package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.mapper.TurnoOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
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
        if (turno.getIdPuesto() == null || turno.getIdSucursalPuesto() == null) return;

        String base = puestoJpaRepository.obtenerNombreLlamada(turno.getIdPuesto(), turno.getIdSucursalPuesto());
        if (base == null) return;

        String correlativo = turno.getIdUsuario() != null
                ? usuarioJpaRepository.obtenerCorrelativo(turno.getIdUsuario(), turno.getIdSucursalPuesto())
                : null;
        String sufijo = correlativo != null ? " " + correlativo : " " + turno.getIdPuesto();

        turno.enriquecerNombreLlamada(base + sufijo);
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
