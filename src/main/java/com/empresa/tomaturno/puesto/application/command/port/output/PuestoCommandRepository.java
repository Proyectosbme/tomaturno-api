package com.empresa.tomaturno.puesto.application.command.port.output;

import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public interface PuestoCommandRepository {
    Puesto save(Puesto puesto);
    Puesto modificar(Puesto puesto);
}
