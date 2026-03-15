package com.coop.tomaturno.sucursal.application.command.port.output;

import com.coop.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;

public interface SucursalEventPublisher {
    void publishSucursalCreada(SucursalCreadaEvent event);
}
