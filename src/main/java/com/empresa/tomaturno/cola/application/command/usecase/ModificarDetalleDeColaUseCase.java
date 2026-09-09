package com.empresa.tomaturno.cola.application.command.usecase;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.port.output.ColaGatewayPort;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaNotFoundException;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;

public class ModificarDetalleDeColaUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaGatewayPort colaGatewayPort;

    public ModificarDetalleDeColaUseCase(ColaCommandRepository colaCommandRepository,
            ColaGatewayPort colaGatewayPort) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaGatewayPort = colaGatewayPort;
    }

    public Cola ejecutar(Long idCola, Long idSucursal, Long idDetalle, String nombre, String codigo, Estado estado,
            Auditoria auditoriaModificacion) {
        Cola cola = colaGatewayPort.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new ColaNotFoundException(idCola, "Cola no encontrada");
        }
        Detalle modificado = cola.modificarDetalle(idDetalle, nombre, codigo, estado, auditoriaModificacion);
        return colaCommandRepository.modificarDetalle(idCola, idSucursal, modificado);
    }
}
