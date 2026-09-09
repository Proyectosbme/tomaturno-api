package com.empresa.tomaturno.cola.application.command.port.input;

import com.empresa.tomaturno.cola.application.command.dto.ResultadoReplicacion;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;

public interface ColaCommandInputPort {
    Cola crear(Cola cola);
    Cola actualizar(Long idCola, Long idSucursal, String nombre, String codigo, Estado estado,
            Auditoria auditoriaModificacion);
    Cola crearDetalle(Long idCola, Long idSucursal, Detalle.Builder detalleBuilder, Auditoria auditoriaCreacion);
    Cola editarDetalleCola(Long idCola, Long idSucursal, Long idDetalle, String nombre, String codigo, Estado estado,
            Auditoria auditoriaModificacion);
    ResultadoReplicacion replicar(Long idSucursalOrigen, Long idSucursalDestino, String usuario);
}
