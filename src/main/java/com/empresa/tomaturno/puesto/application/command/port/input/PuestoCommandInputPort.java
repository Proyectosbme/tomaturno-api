package com.coop.tomaturno.puesto.application.command.port.input;

import com.coop.tomaturno.puesto.dominio.entity.Puesto;

public interface PuestoCommandInputPort {
    Puesto crear(Puesto puesto);
    Puesto actualizar(Long idPuesto, Long idSucursal, Puesto datosActualizados);
}
