package com.coop.tomaturno.configuracion.application.command.usecase;

import com.coop.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.coop.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;
import com.coop.tomaturno.configuracion.dominio.exceptions.ConfiguracionNotFoundException;

import java.time.LocalDateTime;

public class ModificarConfiguracionUseCase {

    private final ConfiguracionCommandRepository commandRepository;
    private final ConfiguracionQueryRepository queryRepository;

    public ModificarConfiguracionUseCase(ConfiguracionCommandRepository commandRepository,
                                         ConfiguracionQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Configuracion ejecutar(Long idConfiguracion, Long idSucursal, Configuracion datosNuevos) {
        Configuracion existente = queryRepository.buscarPorIdYSucursal(idConfiguracion, idSucursal);
        if (existente == null) {
            throw new ConfiguracionNotFoundException(
                    "No se encontró la configuración con id " + idConfiguracion + " y sucursal " + idSucursal);
        }

        existente.modificar(
                datosNuevos.getNombre(),
                datosNuevos.getParametro(),
                datosNuevos.getValorTexto(),
                datosNuevos.getDescripcion(),
                datosNuevos.getEstado()
        );
        existente.auditoriaModificacion("bmarroquin", LocalDateTime.now());
        existente.validarModificacion();

        return commandRepository.modificar(existente);
    }
}
