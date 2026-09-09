
package com.empresa.tomaturno.sucursal.application.command.service;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalGatewayPort;
import com.empresa.tomaturno.sucursal.application.command.usecase.CrearSucursalCaseUse;
import com.empresa.tomaturno.sucursal.application.command.usecase.ModificarSucursalUseCase;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;

public class SucursalCommandService implements SucursalCommandInputPort {
    private final CrearSucursalCaseUse crearSucursalCaseUse;
    private final ModificarSucursalUseCase modificarSucursalUseCase;

    public SucursalCommandService(SucursalCommandRepository sucursalCommandRepository,
            SucursalGatewayPort sucursalGatewayPort,
            SucursalEventPublisher eventPublisher) {
        this.crearSucursalCaseUse = new CrearSucursalCaseUse(sucursalCommandRepository, eventPublisher);
        this.modificarSucursalUseCase = new ModificarSucursalUseCase(sucursalCommandRepository,
                sucursalGatewayPort);
    }

    @Override
    public Sucursal crear(Sucursal sucursal) {
        return crearSucursalCaseUse.ejecutar(sucursal);
    }

    @Override
    public Sucursal actualizar(Long id, String nombre, Contacto contacto, Estado estado,
            Auditoria auditoriaModificacion) {
        return modificarSucursalUseCase.ejecutar(id, nombre, contacto, estado, auditoriaModificacion);
    }
}
