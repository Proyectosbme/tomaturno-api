package com.coop.tomaturno.configuracion.application.command.port.output;

import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;

public interface ConfiguracionCommandRepository {
    Configuracion save(Configuracion configuracion);
    Configuracion modificar(Configuracion configuracion);
}
