package com.coop.tomaturno.configuracion.application.query.service;

import com.coop.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.coop.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.coop.tomaturno.configuracion.application.query.usecase.BuscarConfiguracionPorIdUseCase;
import com.coop.tomaturno.configuracion.application.query.usecase.BuscarConfiguracionPorSucursalUseCase;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;

import java.util.List;

public class ConfiguracionQueryService implements ConfiguracionQueryInputPort {

    private final ConfiguracionQueryRepository queryRepository;
    private final BuscarConfiguracionPorSucursalUseCase buscarPorSucursalUseCase;
    private final BuscarConfiguracionPorIdUseCase buscarPorIdUseCase;

    public ConfiguracionQueryService(ConfiguracionQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
        this.buscarPorSucursalUseCase = new BuscarConfiguracionPorSucursalUseCase(queryRepository);
        this.buscarPorIdUseCase = new BuscarConfiguracionPorIdUseCase(queryRepository);
    }

    @Override
    public List<Configuracion> buscarPorSucursal(Long idSucursal) {
        return buscarPorSucursalUseCase.ejecutar(idSucursal);
    }

    @Override
    public Configuracion buscarPorId(Long idConfiguracion, Long idSucursal) {
        return buscarPorIdUseCase.ejecutar(idConfiguracion, idSucursal);
    }

    @Override
    public Configuracion buscarPorNombre(Long idSucursal, String nombre) {
        return queryRepository.buscarPorNombreYSucursal(idSucursal, nombre);
    }
}
