package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;

import com.empresa.tomaturno.cola.application.command.port.output.ColaGatewayPort;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;

public class ColaGatewayAdapter implements ColaGatewayPort {

    private final ColaQueryRepository colaQueryRepository;

    public ColaGatewayAdapter(ColaQueryRepository colaQueryRepository) {
        this.colaQueryRepository = colaQueryRepository;
    }

    @Override
    public Cola buscarPorIdColaYSucursal(Long idCola, Long idSucursal) {
        return colaQueryRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
    }

    @Override
    public Cola buscarConDetallesPorIdYSucursal(Long idCola, Long idSucursal) {
        return colaQueryRepository.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
    }

    @Override
    public List<Cola> buscarConDetallesPorSucursal(Long idSucursal) {
        return colaQueryRepository.buscarConDetallesPorSucursal(idSucursal);
    }
}
