package com.coop.tomaturno.cola.application.query.usecase;

import com.coop.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.coop.tomaturno.cola.dominio.entity.Cola;
import com.coop.tomaturno.cola.dominio.exceptions.ColaNotFoundException;

public class BuscarColaConDetallesUseCase {

    private final ColaQueryRepository colaRepository;

    public BuscarColaConDetallesUseCase(ColaQueryRepository colaRepository) {
        this.colaRepository = colaRepository;
    }

    public Cola ejecutar(Long idCola, Long idSucursal) {
        Cola cola = colaRepository.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new ColaNotFoundException(idCola, "Cola");
        }
        return cola;
    }
}