package com.empresa.tomaturno.configuracion.application.command.port.input;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public interface ConfiguracionCommandInputPort {
    Configuracion crear(Configuracion configuracion);
    Configuracion actualizar(Long idConfiguracion, Long idSucursal, Configuracion configuracion);
}
