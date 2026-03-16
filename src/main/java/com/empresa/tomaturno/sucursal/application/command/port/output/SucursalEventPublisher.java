package com.empresa.tomaturno.sucursal.application.command.port.output;

import com.empresa.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;

public interface SucursalEventPublisher {
    void publishSucursalCreada(SucursalCreadaEvent event);
    //valor
}
