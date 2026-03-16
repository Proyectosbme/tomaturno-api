package com.empresa.tomaturno.cola.application.command.service;

import com.empresa.tomaturno.cola.application.command.port.input.ColaCommandInputPort;
import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.usecase.CrearColaCaseUse;
import com.empresa.tomaturno.cola.application.command.usecase.CrearDetalleDeColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ModificarColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ReplicarColasUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ReplicarColasUseCase.ResultadoReplicacion;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;

public class ColaCommandService implements ColaCommandInputPort {

    private final CrearColaCaseUse crearColaCaseUse;
    private final ModificarColaUseCase modificarColaUseCase;
    private final CrearDetalleDeColaUseCase crearDetalleDeColaUseCase;
    private final ReplicarColasUseCase replicarColasUseCase;

    public ColaCommandService(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.crearColaCaseUse = new CrearColaCaseUse(colaCommandRepository, colaQueryRepository);
        this.modificarColaUseCase = new ModificarColaUseCase(colaCommandRepository, colaQueryRepository);
        this.crearDetalleDeColaUseCase = new CrearDetalleDeColaUseCase(colaCommandRepository, colaQueryRepository);
        this.replicarColasUseCase = new ReplicarColasUseCase(colaCommandRepository, colaQueryRepository);
    }

    @Override
    public Cola crear(Cola cola) {
        return crearColaCaseUse.ejecutar(cola);
    }

    @Override
    public Cola actualizar(Long idCola, Long idSucursal, Cola datosActualizados) {
        return modificarColaUseCase.ejecutar(idCola, idSucursal, datosActualizados);
    }

    @Override
    public Cola crearDetalle(Long idCola, Long idSucursal, Detalle detalle) {
        return crearDetalleDeColaUseCase.ejecutar(idCola, idSucursal, detalle);
    }

    @Override
    public ResultadoReplicacion replicar(Long idSucursalOrigen, Long idSucursalDestino) {
        return replicarColasUseCase.ejecutar(idSucursalOrigen, idSucursalDestino);
    }
}
