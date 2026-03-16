package com.empresa.tomaturno.cola.application.query.port.input;

import java.util.List;

import com.empresa.tomaturno.cola.dominio.entity.Cola;

public interface ColaQueryInputPort {
    List<Cola> buscarPorFiltro(Long id, Long idSucursal, String nombre);
    Cola buscarConDetalles(Long idCola, Long idSucursal);
    
}
