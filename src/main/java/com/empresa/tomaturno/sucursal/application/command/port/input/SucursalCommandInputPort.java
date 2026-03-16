package com.empresa.tomaturno.sucursal.application.command.port.input;

import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

public interface SucursalCommandInputPort {
    Sucursal crear(Sucursal sucursal);
    Sucursal actualizar(Long id, Sucursal datosActualizados);
}
