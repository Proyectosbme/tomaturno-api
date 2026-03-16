package com.empresa.tomaturno.framework.adapters.event;

import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;
import com.empresa.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;

import io.vertx.mutiny.core.eventbus.EventBus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SucursalEventPublisherAdapter implements SucursalEventPublisher {
    private final EventBus eventBus;

    @Inject
    public SucursalEventPublisherAdapter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void publishSucursalCreada(SucursalCreadaEvent event) {
        eventBus.publish("sucursal.creada", event);
    }
}
