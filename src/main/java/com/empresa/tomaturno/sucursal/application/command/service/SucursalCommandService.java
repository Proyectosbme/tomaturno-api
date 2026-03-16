
package com.empresa.tomaturno.sucursal.application.command.service;

import com.empresa.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;
import com.empresa.tomaturno.sucursal.application.command.usecase.CrearSucursalCaseUse;
import com.empresa.tomaturno.sucursal.application.command.usecase.ModificarSucursalUseCase;
import com.empresa.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;


public class SucursalCommandService implements SucursalCommandInputPort {
    private final CrearSucursalCaseUse crearSucursalCaseUse;
    private final ModificarSucursalUseCase modificarSucursalUseCase;

    public SucursalCommandService(SucursalCommandRepository sucursalCommandRepository,
                                  SucursalQueryRepository sucursalQueryRepository,
                                  SucursalEventPublisher eventPublisher) {
        this.crearSucursalCaseUse = new CrearSucursalCaseUse(sucursalCommandRepository, eventPublisher);
        this.modificarSucursalUseCase = new ModificarSucursalUseCase(sucursalCommandRepository, sucursalQueryRepository);
    }


    @Override
    public Sucursal crear(Sucursal sucursal) {
        return crearSucursalCaseUse.ejecutar(sucursal);
    }

    @Override
    public Sucursal actualizar(Long id, Sucursal datosActualizados) {
        return modificarSucursalUseCase.ejecutar(id, datosActualizados);
    }
}
