package com.empresa.tomaturno.puesto.application.command.port.input;

import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public interface PuestoCommandInputPort {
    Puesto crear(Puesto puesto, String usuario);
    Puesto actualizar(Long idPuesto, Long idSucursal, Puesto datosActualizados, String usuario);
}
