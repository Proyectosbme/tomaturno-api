package com.empresa.tomaturno.empresa.application.command.port.output;

import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

/**
 * Lectura que el lado command necesita (resolver la empresa a modificar) sin
 * depender del EmpresaQueryRepository completo, que pertenece al lado query.
 */
public interface EmpresaGatewayPort {
    Empresa obtener();
}
