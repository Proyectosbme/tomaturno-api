package com.empresa.tomaturno.configuracion.application.command.service;

import com.empresa.tomaturno.configuracion.application.command.port.input.ConfiguracionCommandInputPort;
import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.application.command.usecase.CrearConfiguracionUseCase;
import com.empresa.tomaturno.configuracion.application.command.usecase.ModificarConfiguracionUseCase;
import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public class ConfiguracionCommandService implements ConfiguracionCommandInputPort {

    private final CrearConfiguracionUseCase crearUseCase;
    private final ModificarConfiguracionUseCase modificarUseCase;

    public ConfiguracionCommandService(ConfiguracionCommandRepository commandRepository,
                                       ConfiguracionQueryRepository queryRepository) {
        this.crearUseCase = new CrearConfiguracionUseCase(commandRepository);
        this.modificarUseCase = new ModificarConfiguracionUseCase(commandRepository, queryRepository);
    }

    @Override
    public Configuracion crear(Configuracion configuracion, String usuario) {
        return crearUseCase.ejecutar(configuracion, usuario);
    }

    @Override
    public Configuracion actualizar(Long idConfiguracion, Long idSucursal, Configuracion configuracion, String usuario) {
        return modificarUseCase.ejecutar(idConfiguracion, idSucursal, configuracion, usuario);
    }
}
