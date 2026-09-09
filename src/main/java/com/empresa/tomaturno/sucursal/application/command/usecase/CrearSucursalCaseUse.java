package com.empresa.tomaturno.sucursal.application.command.usecase;

import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;

public class CrearSucursalCaseUse {

    private final SucursalCommandRepository sucursalCommandRepository;
    private final SucursalEventPublisher eventPublisher;

    public CrearSucursalCaseUse(SucursalCommandRepository sucursalCommandRepository,
                                SucursalEventPublisher eventPublisher) {
        this.sucursalCommandRepository = sucursalCommandRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * La sucursal ya llega con auditoriaCreacion armada (Sucursal.of(builder) la validó);
     * este caso de uso no la arma.
     */
    public Sucursal ejecutar(Sucursal sucursal) {
        Sucursal creada = sucursalCommandRepository.save(sucursal);
        eventPublisher.publishSucursalCreada(new SucursalCreadaEvent(creada.getIdentificador()));
        return creada;
    }

}
