package com.empresa.tomaturno.puesto.application.command.port.input;

import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.puesto.dominio.vo.Estado;

public interface PuestoCommandInputPort {
    Puesto crear(Puesto puesto);
    Puesto actualizar(Long idPuesto, Long idSucursal, String nombre, String nombreLlamada, Estado estado,
            Auditoria auditoriaModificacion);
}
