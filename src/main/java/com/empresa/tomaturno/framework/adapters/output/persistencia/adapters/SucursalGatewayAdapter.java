package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalGatewayPort;
import com.empresa.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

public class SucursalGatewayAdapter implements SucursalGatewayPort {

    private final SucursalQueryRepository sucursalQueryRepository;

    public SucursalGatewayAdapter(SucursalQueryRepository sucursalQueryRepository) {
        this.sucursalQueryRepository = sucursalQueryRepository;
    }

    @Override
    public Sucursal buscarPorId(Long id) {
        return sucursalQueryRepository.buscarPorId(id);
    }
}
