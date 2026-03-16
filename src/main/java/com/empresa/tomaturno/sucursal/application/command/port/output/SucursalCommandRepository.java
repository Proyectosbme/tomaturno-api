package com.empresa.tomaturno.sucursal.application.command.port.output;

import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

public interface SucursalCommandRepository {
    
    Sucursal save(Sucursal sucursal);

    Sucursal modificar(Sucursal sucursal);
}
