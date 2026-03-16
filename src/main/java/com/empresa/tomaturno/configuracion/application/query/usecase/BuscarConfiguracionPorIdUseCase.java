package com.empresa.tomaturno.configuracion.application.query.usecase;

import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.exceptions.ConfiguracionNotFoundException;

public class BuscarConfiguracionPorIdUseCase {

    private final ConfiguracionQueryRepository queryRepository;

    public BuscarConfiguracionPorIdUseCase(ConfiguracionQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Configuracion ejecutar(Long idConfiguracion, Long idSucursal) {
        Configuracion config = queryRepository.buscarPorIdYSucursal(idConfiguracion, idSucursal);
        if (config == null) {
            throw new ConfiguracionNotFoundException(
                    "No se encontró la configuración con id " + idConfiguracion + " y sucursal " + idSucursal);
        }
        return config;
    }
}
