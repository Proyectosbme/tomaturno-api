package com.empresa.tomaturno.sucursal.application.command.usecase;

import java.time.LocalDateTime;

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

    public Sucursal ejecutar(Sucursal sucursal) {
        sucursal.auditoriaCreacion("bmarroquin", LocalDateTime.now());
        Sucursal creada = sucursalCommandRepository.save(sucursal);
        eventPublisher.publishSucursalCreada(new SucursalCreadaEvent(creada.getIdentificador()));
        return creada;
    }

}
