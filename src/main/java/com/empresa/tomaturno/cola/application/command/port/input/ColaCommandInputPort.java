package com.coop.tomaturno.cola.application.command.port.input;


import com.coop.tomaturno.cola.dominio.entity.Cola;
import com.coop.tomaturno.cola.dominio.entity.Detalle;
import com.coop.tomaturno.cola.application.command.usecase.ReplicarColasUseCase.ResultadoReplicacion;

public interface ColaCommandInputPort {
    Cola crear(Cola cola);
    Cola actualizar(Long idCola, Long idSucursal, Cola datosActualizados);
    Cola crearDetalle(Long idCola, Long idSucursal, Detalle detalle);
    ResultadoReplicacion replicar(Long idSucursalOrigen, Long idSucursalDestino);
}
