package com.empresa.tomaturno.estadooperador.application.query.service;

import com.empresa.tomaturno.estadooperador.application.query.port.input.EstadoOperadorQueryInputPort;
import com.empresa.tomaturno.estadooperador.application.query.port.output.EstadoOperadorQueryRepository;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public class EstadoOperadorQueryService implements EstadoOperadorQueryInputPort {

    private final EstadoOperadorQueryRepository queryRepository;

    public EstadoOperadorQueryService(EstadoOperadorQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public EstadoOperador buscarVigente(Long idUsuario, Long idSucursal) {
        return queryRepository.buscarVigente(idUsuario, idSucursal);
    }
}
