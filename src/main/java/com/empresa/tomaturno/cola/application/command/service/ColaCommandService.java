package com.empresa.tomaturno.cola.application.command.service;

import com.empresa.tomaturno.cola.application.command.dto.ResultadoReplicacion;
import com.empresa.tomaturno.cola.application.command.port.input.ColaCommandInputPort;
import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.port.output.ColaGatewayPort;
import com.empresa.tomaturno.cola.application.command.usecase.CrearColaCaseUse;
import com.empresa.tomaturno.cola.application.command.usecase.CrearDetalleDeColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ModificarColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ModificarDetalleDeColaUseCase;
import com.empresa.tomaturno.cola.application.command.usecase.ReplicarColasUseCase;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;

public class ColaCommandService implements ColaCommandInputPort {

    private final CrearColaCaseUse crearColaCaseUse;
    private final ModificarColaUseCase modificarColaUseCase;
    private final CrearDetalleDeColaUseCase crearDetalleDeColaUseCase;
    private final ReplicarColasUseCase replicarColasUseCase;
    private final ModificarDetalleDeColaUseCase modificarDetalleDeColaUseCase;

    public ColaCommandService(ColaCommandRepository colaCommandRepository,
            ColaGatewayPort colaGatewayPort) {
        this.crearColaCaseUse = new CrearColaCaseUse(colaCommandRepository, colaGatewayPort);
        this.modificarColaUseCase = new ModificarColaUseCase(colaCommandRepository, colaGatewayPort);
        this.crearDetalleDeColaUseCase = new CrearDetalleDeColaUseCase(colaCommandRepository, colaGatewayPort);
        this.replicarColasUseCase = new ReplicarColasUseCase(colaCommandRepository, colaGatewayPort);
        this.modificarDetalleDeColaUseCase = new ModificarDetalleDeColaUseCase(colaCommandRepository, colaGatewayPort);
    }

    @Override
    public Cola crear(Cola cola) {
        return crearColaCaseUse.ejecutar(cola);
    }

    @Override
    public Cola actualizar(Long idCola, Long idSucursal, String nombre, String codigo, Estado estado,
            Auditoria auditoriaModificacion) {
        return modificarColaUseCase.ejecutar(idCola, idSucursal, nombre, codigo, estado, auditoriaModificacion);
    }

    @Override
    public Cola crearDetalle(Long idCola, Long idSucursal, Detalle.Builder detalleBuilder,
            Auditoria auditoriaCreacion) {
        return crearDetalleDeColaUseCase.ejecutar(idCola, idSucursal, detalleBuilder, auditoriaCreacion);
    }

    @Override
    public ResultadoReplicacion replicar(Long idSucursalOrigen, Long idSucursalDestino, String usuario) {
        return replicarColasUseCase.ejecutar(idSucursalOrigen, idSucursalDestino, usuario);
    }

    @Override
    public Cola editarDetalleCola(Long idCola, Long idSucursal, Long idDetalle, String nombre, String codigo,
            Estado estado, Auditoria auditoriaModificacion) {
        return modificarDetalleDeColaUseCase.ejecutar(idCola, idSucursal, idDetalle, nombre, codigo, estado,
                auditoriaModificacion);
    }
}
