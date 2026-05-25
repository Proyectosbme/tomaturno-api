package com.empresa.tomaturno.turno.application.query.port.output;

public interface TurnoColaPort {
    Long resolverDetalleParaReasignacion(Long idCola, Long idSucursal, Long idDetalle);
}
