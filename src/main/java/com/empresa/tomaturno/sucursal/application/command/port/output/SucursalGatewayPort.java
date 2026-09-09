package com.empresa.tomaturno.sucursal.application.command.port.output;

import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

/**
 * Lectura que el lado command necesita (resolver la sucursal a modificar) sin
 * depender del SucursalQueryRepository completo, que pertenece al lado query.
 */
public interface SucursalGatewayPort {
    Sucursal buscarPorId(Long id);
}
