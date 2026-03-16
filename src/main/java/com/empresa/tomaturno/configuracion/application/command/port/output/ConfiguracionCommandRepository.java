package com.empresa.tomaturno.configuracion.application.command.port.output;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public interface ConfiguracionCommandRepository {
    Configuracion save(Configuracion configuracion);
    Configuracion modificar(Configuracion configuracion);
}
