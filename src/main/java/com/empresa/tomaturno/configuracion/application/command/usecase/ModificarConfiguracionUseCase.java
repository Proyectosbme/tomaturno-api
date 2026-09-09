package com.empresa.tomaturno.configuracion.application.command.usecase;

import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionGatewayPort;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.exceptions.ConfiguracionNotFoundException;
import com.empresa.tomaturno.configuracion.dominio.vo.Auditoria;
import com.empresa.tomaturno.configuracion.dominio.vo.Estado;

public class ModificarConfiguracionUseCase {

    private final ConfiguracionCommandRepository commandRepository;
    private final ConfiguracionGatewayPort configuracionGatewayPort;

    public ModificarConfiguracionUseCase(ConfiguracionCommandRepository commandRepository,
            ConfiguracionGatewayPort configuracionGatewayPort) {
        this.commandRepository = commandRepository;
        this.configuracionGatewayPort = configuracionGatewayPort;
    }

    public Configuracion ejecutar(Long idConfiguracion, Long idSucursal, Integer parametro, String descripcion,
            Estado estado, Auditoria auditoriaModificacion) {
        Configuracion existente = configuracionGatewayPort.buscarPorIdYSucursal(idConfiguracion, idSucursal);
        if (existente == null)
            throw new ConfiguracionNotFoundException(
                    "No se encontró la configuración con id " + idConfiguracion + " y sucursal " + idSucursal);

        existente.modificar(parametro, descripcion, estado, auditoriaModificacion);
        return commandRepository.modificar(existente);
    }
}
