package com.empresa.tomaturno.cola.application.command.port.output;

import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;


public interface ColaCommandRepository {
    
    Cola save(Cola cola);

    Cola modificar(Cola cola);

    Cola guardarDetalle(Long idCola, Long idSucursal, Detalle detalle);
    Cola modificarDetalle(Long idCola, Long idSucursal, Detalle detalleActualizado);
    Cola replicarCola(Cola colaOrigen, Long idSucursalDestino,String usuario);
}
