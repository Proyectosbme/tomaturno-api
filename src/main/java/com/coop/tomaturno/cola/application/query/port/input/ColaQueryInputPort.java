package com.coop.tomaturno.cola.application.query.port.input;

import java.util.List;
import com.coop.tomaturno.cola.dominio.entity.Cola;

public interface ColaQueryInputPort {
    List<Cola> buscarPorFiltro(Long id, Long idSucursal, String nombre);
    Cola buscarConDetalles(Long idCola, Long idSucursal);
    
}
