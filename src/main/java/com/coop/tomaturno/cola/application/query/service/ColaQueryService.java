package com.coop.tomaturno.cola.application.query.service;

import java.util.List;

import com.coop.tomaturno.cola.application.query.port.input.ColaQueryInputPort;
import com.coop.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.coop.tomaturno.cola.application.query.usecase.BuscarColaConDetallesUseCase;
import com.coop.tomaturno.cola.application.query.usecase.BuscarColaPorFiltrosUseCase;
import com.coop.tomaturno.cola.dominio.entity.Cola;

public class ColaQueryService implements ColaQueryInputPort {

    private final BuscarColaPorFiltrosUseCase buscarColaPorFiltrosUseCase;
    private final BuscarColaConDetallesUseCase buscarColaConDetallesUseCase;

    public ColaQueryService(ColaQueryRepository colaQueryRepository) {
        this.buscarColaPorFiltrosUseCase = new BuscarColaPorFiltrosUseCase(colaQueryRepository);
        this.buscarColaConDetallesUseCase = new BuscarColaConDetallesUseCase(colaQueryRepository);
    }

    @Override
    public List<Cola> buscarPorFiltro(Long id, Long idSucursal, String nombre) {
        return buscarColaPorFiltrosUseCase.ejecutar(id, idSucursal, nombre);
    }

    // Agrega el método:
    @Override
    public Cola buscarConDetalles(Long idCola, Long idSucursal) {
        return buscarColaConDetallesUseCase.ejecutar(idCola, idSucursal);
    }
}
