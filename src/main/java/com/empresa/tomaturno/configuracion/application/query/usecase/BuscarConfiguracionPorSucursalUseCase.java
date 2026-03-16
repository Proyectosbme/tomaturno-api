package com.coop.tomaturno.configuracion.application.query.usecase;

import com.coop.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;

import java.util.List;

public class BuscarConfiguracionPorSucursalUseCase {

    private final ConfiguracionQueryRepository queryRepository;

    public BuscarConfiguracionPorSucursalUseCase(ConfiguracionQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Configuracion> ejecutar(Long idSucursal) {
        return queryRepository.buscarPorSucursal(idSucursal);
    }
}
