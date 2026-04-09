package com.empresa.tomaturno.configuracion.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.exceptions.ConfiguracionNotFoundException;

public class ModificarConfiguracionUseCase {

    private final ConfiguracionCommandRepository commandRepository;
    private final ConfiguracionQueryRepository queryRepository;

    public ModificarConfiguracionUseCase(ConfiguracionCommandRepository commandRepository,
            ConfiguracionQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Configuracion ejecutar(Long idConfiguracion, Long idSucursal, Configuracion datosNuevos, String usuario) {
        Configuracion existente = queryRepository.buscarPorIdYSucursal(idConfiguracion, idSucursal);
        if (existente == null)
            throw new ConfiguracionNotFoundException(
                    "No se encontró la configuración con id " + idConfiguracion + " y sucursal " + idSucursal);

        existente.modificar(
                datosNuevos.getNombre(),
                datosNuevos.getParametro(),
                datosNuevos.getValorTexto(),
                datosNuevos.getDescripcion(),
                datosNuevos.getEstado(), usuario);
        return commandRepository.modificar(existente);
    }
}
