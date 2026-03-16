package com.empresa.tomaturno.configuracion.application.query.usecase;

import java.util.List;

import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public class BuscarConfiguracionPorSucursalUseCase {

    private final ConfiguracionQueryRepository queryRepository;

    public BuscarConfiguracionPorSucursalUseCase(ConfiguracionQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Configuracion> ejecutar(Long idSucursal) {
        return queryRepository.buscarPorSucursal(idSucursal);
    }
}
