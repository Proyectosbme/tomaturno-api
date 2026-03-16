package com.coop.tomaturno.cola.application.query.port.output;

import java.util.List;
import com.coop.tomaturno.cola.dominio.entity.Cola;

public interface ColaQueryRepository {

    Cola buscarPorIdColaYSucursal(Long idCola, Long idSucursal);

    List<Cola> buscarPorFiltro(Long id, Long idSucursal, String nombre);

    Cola buscarConDetallesPorIdYSucursal(Long idCola, Long idSucursal);

    /** Para replicar: trae todas las colas con detalles de una sucursal */
    List<Cola> buscarConDetallesPorSucursal(Long idSucursal);

    /** Validación unicidad cola: ¿ya existe una cola con ese nombre en la sucursal? */
    boolean existeNombreEnSucursal(Long idSucursal, String nombre);

    /** Validación unicidad detalle: ¿ya existe un detalle con ese nombre en la cola? */
    boolean existeNombreDetalleEnCola(Long idCola, Long idSucursal, String nombreDetalle);
}
