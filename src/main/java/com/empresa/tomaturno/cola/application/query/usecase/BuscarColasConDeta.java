package com.empresa.tomaturno.cola.application.query.usecase;

import java.util.List;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
public class BuscarColasConDeta {
     private final ColaQueryRepository colaRepository;

    public BuscarColasConDeta(ColaQueryRepository colaRepository) {
        this.colaRepository = colaRepository;
    }

   public List<Cola> ejecutar( Long idSucursal) {
        return colaRepository.buscarColasQueTienenDetalles(idSucursal);
    }
}
