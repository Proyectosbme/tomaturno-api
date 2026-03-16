package com.coop.tomaturno.sucursal.application.command.port.output;

import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;

public interface SucursalCommandRepository {
    
    Sucursal save(Sucursal sucursal);

    Sucursal modificar(Sucursal sucursal);
}
