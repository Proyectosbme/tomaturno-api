package com.coop.tomaturno.sucursal.dominio.event;

public class SucursalCreadaEvent {
    private final Long sucursalId;

    public SucursalCreadaEvent(Long sucursalId) {
        this.sucursalId = sucursalId;
    }

    public Long getSucursalId() {
        return sucursalId;
    }
}
