package com.empresa.tomaturno.cola.application.command.service;

import com.empresa.tomaturno.cola.DTO.ResultadoReplicacion;
import com.empresa.tomaturno.cola.application.command.port.input.ColaCommandInputPort;
import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.usecase.CrearColaCaseUse;
import com.empresa.tomaturno.cola.application.command.usecase.CrearDetalleDeColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ModificarColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ModificarDetalleDeColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ReplicarColasUseCase;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;

public class ColaCommandService implements ColaCommandInputPort {

    private final CrearColaCaseUse crearColaCaseUse;
    private final ModificarColaUseCase modificarColaUseCase;
    private final CrearDetalleDeColaUseCase crearDetalleDeColaUseCase;
    private final ReplicarColasUseCase replicarColasUseCase;
    private final ModificarDetalleDeColaUseCase modificarDetalleDeColaUseCase;

    public ColaCommandService(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.crearColaCaseUse = new CrearColaCaseUse(colaCommandRepository, colaQueryRepository);
        this.modificarColaUseCase = new ModificarColaUseCase(colaCommandRepository, colaQueryRepository);
        this.crearDetalleDeColaUseCase = new CrearDetalleDeColaUseCase(colaCommandRepository, colaQueryRepository);
        this.replicarColasUseCase = new ReplicarColasUseCase(colaCommandRepository, colaQueryRepository);
        this.modificarDetalleDeColaUseCase = new ModificarDetalleDeColaUseCase(colaCommandRepository, colaQueryRepository);
    }

    @Override
    public Cola crear(Cola cola ,String usuario) {
        return crearColaCaseUse.ejecutar(cola, usuario);
    }

    @Override
    public Cola actualizar(Long idCola, Long idSucursal, Cola datosActualizados, String usuario) {
        return modificarColaUseCase.ejecutar(idCola, idSucursal, datosActualizados, usuario);
    }

    @Override
    public Cola crearDetalle(Long idCola, Long idSucursal, Detalle detalle,String usuario) {
        return crearDetalleDeColaUseCase.ejecutar(idCola, idSucursal, detalle, usuario);
    }

    @Override
    public ResultadoReplicacion replicar(Long idSucursalOrigen, Long idSucursalDestino,String usuario) {
        return replicarColasUseCase.ejecutar(idSucursalOrigen, idSucursalDestino, usuario);
    }

    @Override
    public Cola editarDetalleCola(Long idCola, Long idSucursal, Long idDetalle, Detalle detalleActualizado, String usuario) {
       return modificarDetalleDeColaUseCase.ejecutar(idCola, idSucursal, idDetalle, detalleActualizado, usuario);
    }
}
