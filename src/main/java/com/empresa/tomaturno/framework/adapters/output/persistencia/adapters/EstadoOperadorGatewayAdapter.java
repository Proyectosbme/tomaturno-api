package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.estadooperador.application.command.port.output.EstadoOperadorGatewayPort;
import com.empresa.tomaturno.estadooperador.application.query.port.output.EstadoOperadorQueryRepository;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public class EstadoOperadorGatewayAdapter implements EstadoOperadorGatewayPort {

    private final EstadoOperadorQueryRepository estadoOperadorQueryRepository;

    public EstadoOperadorGatewayAdapter(EstadoOperadorQueryRepository estadoOperadorQueryRepository) {
        this.estadoOperadorQueryRepository = estadoOperadorQueryRepository;
    }

    @Override
    public EstadoOperador buscarVigente(Long idUsuario, Long idSucursal) {
        return estadoOperadorQueryRepository.buscarVigente(idUsuario, idSucursal);
    }
}
