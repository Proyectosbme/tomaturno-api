package com.empresa.tomaturno.cola.application.command.port.input;


import com.empresa.tomaturno.cola.application.command.usecase.ReplicarColasUseCase.ResultadoReplicacion;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;

public interface ColaCommandInputPort {
    Cola crear(Cola cola);
    Cola actualizar(Long idCola, Long idSucursal, Cola datosActualizados);
    Cola crearDetalle(Long idCola, Long idSucursal, Detalle detalle);
    ResultadoReplicacion replicar(Long idSucursalOrigen, Long idSucursalDestino);
}
