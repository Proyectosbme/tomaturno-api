package com.empresa.tomaturno.cola.application.command.port.output;

import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;


public interface ColaCommandRepository {
    
    Cola save(Cola cola);

    Cola modificar(Cola cola);

    Cola guardarDetalle(Long idCola, Long idSucursal, Detalle detalle);


    /** Persiste una cola con todos sus detalles en la sucursal destino */
    Cola replicarCola(Cola colaOrigen, Long idSucursalDestino);
}
