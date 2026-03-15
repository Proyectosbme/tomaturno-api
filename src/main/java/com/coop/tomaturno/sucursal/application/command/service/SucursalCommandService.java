
package com.coop.tomaturno.sucursal.application.command.service;

import com.coop.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.coop.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.coop.tomaturno.sucursal.application.command.usecase.CrearSucursalCaseUse;
import com.coop.tomaturno.sucursal.application.command.usecase.ModificarSucursalUseCase;
import com.coop.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;
import com.coop.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;
import com.coop.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;


public class SucursalCommandService implements SucursalCommandInputPort {
    private final CrearSucursalCaseUse crearSucursalCaseUse;
    private final ModificarSucursalUseCase modificarSucursalUseCase;
    private final SucursalEventPublisher eventPublisher;

    public SucursalCommandService(SucursalCommandRepository sucursalCommandRepository,
                                  SucursalQueryRepository sucursalQueryRepository,
                                  SucursalEventPublisher eventPublisher) {
        this.crearSucursalCaseUse = new CrearSucursalCaseUse(sucursalCommandRepository);
        this.modificarSucursalUseCase = new ModificarSucursalUseCase(sucursalCommandRepository, sucursalQueryRepository);
        this.eventPublisher = eventPublisher;
    }


    @Override
    public Sucursal crear(Sucursal sucursal) {
        Sucursal creada = crearSucursalCaseUse.ejecutar(sucursal);
        // Publicar evento de dominio usando la interfaz
        eventPublisher.publishSucursalCreada(new SucursalCreadaEvent(creada.getIdentificador()));
        return creada;
    }

    @Override
    public Sucursal actualizar(Long id, Sucursal datosActualizados) {
        return modificarSucursalUseCase.ejecutar(id, datosActualizados);
    }
}
