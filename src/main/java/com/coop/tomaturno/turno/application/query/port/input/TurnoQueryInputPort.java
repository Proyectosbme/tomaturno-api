package com.coop.tomaturno.turno.application.query.port.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.coop.tomaturno.turno.dominio.entity.Turno;

public interface TurnoQueryInputPort {
    Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno);
    List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha);
}
