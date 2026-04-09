package com.empresa.tomaturno.configuracion.application.command.usecase;

import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public class CrearConfiguracionUseCase {

    private final ConfiguracionCommandRepository commandRepository;

    public CrearConfiguracionUseCase(ConfiguracionCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Configuracion ejecutar(Configuracion configuracion, String usuario) {
        configuracion.crear(usuario);
        return commandRepository.save(configuracion);
    }
}
