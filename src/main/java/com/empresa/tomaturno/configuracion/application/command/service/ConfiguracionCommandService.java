package com.empresa.tomaturno.configuracion.application.command.service;

import com.empresa.tomaturno.configuracion.application.command.port.input.ConfiguracionCommandInputPort;
import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionGatewayPort;
import com.empresa.tomaturno.configuracion.application.command.usecase.CrearConfiguracionUseCase;
import com.empresa.tomaturno.configuracion.application.command.usecase.ModificarConfiguracionUseCase;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.vo.Auditoria;
import com.empresa.tomaturno.configuracion.dominio.vo.Estado;

public class ConfiguracionCommandService implements ConfiguracionCommandInputPort {

    private final CrearConfiguracionUseCase crearUseCase;
    private final ModificarConfiguracionUseCase modificarUseCase;

    public ConfiguracionCommandService(ConfiguracionCommandRepository commandRepository,
                                       ConfiguracionGatewayPort configuracionGatewayPort) {
        this.crearUseCase = new CrearConfiguracionUseCase(commandRepository);
        this.modificarUseCase = new ModificarConfiguracionUseCase(commandRepository, configuracionGatewayPort);
    }

    @Override
    public Configuracion crear(Configuracion configuracion) {
        return crearUseCase.ejecutar(configuracion);
    }

    @Override
    public Configuracion actualizar(Long idConfiguracion, Long idSucursal, Integer parametro, String descripcion,
            Estado estado, Auditoria auditoriaModificacion) {
        return modificarUseCase.ejecutar(idConfiguracion, idSucursal, parametro, descripcion, estado,
                auditoriaModificacion);
    }
}
