package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoColaPort;

public class TurnoColaAdapter implements TurnoColaPort {

    private final ColaQueryRepository colaQueryRepository;

    public TurnoColaAdapter(ColaQueryRepository colaQueryRepository) {
        this.colaQueryRepository = colaQueryRepository;
    }

    @Override
    public Long resolverDetalleParaReasignacion(Long idCola, Long idSucursal, Long idDetalle) {
        Cola cola = colaQueryRepository.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
        if (cola == null) {
            return null;
        }
        return cola.resolverDetalleReasignacion(idDetalle);
    }
}
