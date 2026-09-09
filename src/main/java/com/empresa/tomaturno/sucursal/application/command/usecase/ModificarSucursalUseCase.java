package com.empresa.tomaturno.sucursal.application.command.usecase;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalGatewayPort;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalNotFoundException;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;

public class ModificarSucursalUseCase {

    private final SucursalCommandRepository sucursalCommandRepository;
    private final SucursalGatewayPort sucursalGatewayPort;

    public ModificarSucursalUseCase(SucursalCommandRepository sucursalCommandRepository,
            SucursalGatewayPort sucursalGatewayPort) {
        this.sucursalCommandRepository = sucursalCommandRepository;
        this.sucursalGatewayPort = sucursalGatewayPort;
    }

    public Sucursal ejecutar(Long id, String nombre, Contacto contacto, Estado estado,
            Auditoria auditoriaModificacion) {
        Sucursal sucursal = sucursalGatewayPort.buscarPorId(id);
        if (sucursal == null)
            throw new SucursalNotFoundException(id, "Sucursal");
        sucursal.modificar(nombre, contacto, estado, auditoriaModificacion);
        return sucursalCommandRepository.modificar(sucursal);
    }
}
