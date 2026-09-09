package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionGatewayPort;
import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public class ConfiguracionGatewayAdapter implements ConfiguracionGatewayPort {

    private final ConfiguracionQueryRepository configuracionQueryRepository;

    public ConfiguracionGatewayAdapter(ConfiguracionQueryRepository configuracionQueryRepository) {
        this.configuracionQueryRepository = configuracionQueryRepository;
    }

    @Override
    public Configuracion buscarPorIdYSucursal(Long idConfiguracion, Long idSucursal) {
        return configuracionQueryRepository.buscarPorIdYSucursal(idConfiguracion, idSucursal);
    }
}
