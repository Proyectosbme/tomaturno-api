package com.empresa.tomaturno.cola.application.query.service;

import java.util.List;

import com.empresa.tomaturno.cola.application.query.port.input.ColaQueryInputPort;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.application.query.usecase.BuscarColaConDetallesUseCase;
import com.empresa.tomaturno.cola.application.query.usecase.BuscarColaPorFiltrosUseCase;
import com.empresa.tomaturno.cola.application.query.usecase.BuscarColasConDeta;
import com.empresa.tomaturno.cola.dominio.entity.Cola;

public class ColaQueryService implements ColaQueryInputPort {

    private final BuscarColaPorFiltrosUseCase buscarColaPorFiltrosUseCase;
    private final BuscarColaConDetallesUseCase buscarColaConDetallesUseCase;
    private final BuscarColasConDeta buscarColasConDeta;

    public ColaQueryService(ColaQueryRepository colaQueryRepository) {
        this.buscarColaPorFiltrosUseCase = new BuscarColaPorFiltrosUseCase(colaQueryRepository);
        this.buscarColaConDetallesUseCase = new BuscarColaConDetallesUseCase(colaQueryRepository);
        this.buscarColasConDeta = new BuscarColasConDeta(colaQueryRepository);
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

    @Override
    public List<Cola> buscarColasConDetalle(Long idSucursal) {
       return buscarColasConDeta.ejecutar(idSucursal);
    }
}
