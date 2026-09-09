package com.empresa.tomaturno.configuracion.application.command.port.output;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

/**
 * Lectura que el lado command necesita (resolver la configuración a modificar) sin
 * depender del ConfiguracionQueryRepository completo, que pertenece al lado query.
 */
public interface ConfiguracionGatewayPort {
    Configuracion buscarPorIdYSucursal(Long idConfiguracion, Long idSucursal);
}
