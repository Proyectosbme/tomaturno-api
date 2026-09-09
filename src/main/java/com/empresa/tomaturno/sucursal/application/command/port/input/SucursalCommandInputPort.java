package com.empresa.tomaturno.sucursal.application.command.port.input;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;

public interface SucursalCommandInputPort {
    Sucursal crear(Sucursal sucursal);
    Sucursal actualizar(Long id, String nombre, Contacto contacto, Estado estado, Auditoria auditoriaModificacion);
}
