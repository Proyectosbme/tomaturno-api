package com.empresa.tomaturno.cola.application.command.port.input;



import com.empresa.tomaturno.cola.DTO.ResultadoReplicacion;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;

public interface ColaCommandInputPort {
    Cola crear(Cola cola,String usuario);
    Cola actualizar(Long idCola, Long idSucursal, Cola datosActualizados, String usuario);
    Cola crearDetalle(Long idCola, Long idSucursal, Detalle detalle, String usuario);
    Cola editarDetalleCola(Long idCola, Long idSucursal, Long idDetalle, Detalle detalleActualizado, String usuario);
    ResultadoReplicacion replicar(Long idSucursalOrigen, Long idSucursalDestino,String usuario);
}
