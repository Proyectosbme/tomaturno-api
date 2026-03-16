package com.coop.tomaturno.configuracion.application.command.port.input;

import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;

public interface ConfiguracionCommandInputPort {
    Configuracion crear(Configuracion configuracion);
    Configuracion actualizar(Long idConfiguracion, Long idSucursal, Configuracion configuracion);
}
