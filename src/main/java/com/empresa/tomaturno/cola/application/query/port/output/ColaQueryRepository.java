package com.empresa.tomaturno.cola.application.query.port.output;

import java.util.List;

import com.empresa.tomaturno.cola.dominio.entity.Cola;

public interface ColaQueryRepository {

    Cola buscarPorIdColaYSucursal(Long idCola, Long idSucursal);

    List<Cola> buscarPorFiltro(Long id, Long idSucursal, String nombre);

    Cola buscarConDetallesPorIdYSucursal(Long idCola, Long idSucursal);

    /** Para replicar: trae todas las colas con detalles de una sucursal */
    List<Cola> buscarConDetallesPorSucursal(Long idSucursal);

    List<Cola> buscarColasQueTienenDetalles(Long idSucursal);
}
