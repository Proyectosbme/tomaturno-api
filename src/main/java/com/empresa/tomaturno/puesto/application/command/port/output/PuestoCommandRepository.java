package com.coop.tomaturno.puesto.application.command.port.output;

import com.coop.tomaturno.puesto.dominio.entity.Puesto;

public interface PuestoCommandRepository {
    Puesto save(Puesto puesto);
    Puesto modificar(Puesto puesto);
}
