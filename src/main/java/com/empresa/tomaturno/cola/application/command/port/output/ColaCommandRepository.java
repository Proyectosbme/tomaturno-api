package com.coop.tomaturno.cola.application.command.port.output;

import com.coop.tomaturno.cola.dominio.entity.Cola;
import com.coop.tomaturno.cola.dominio.entity.Detalle;


public interface ColaCommandRepository {
    
    Cola save(Cola cola);

    Cola modificar(Cola cola);

    Cola guardarDetalle(Long idCola, Long idSucursal, Detalle detalle);


    /** Persiste una cola con todos sus detalles en la sucursal destino */
    Cola replicarCola(Cola colaOrigen, Long idSucursalDestino);
}
