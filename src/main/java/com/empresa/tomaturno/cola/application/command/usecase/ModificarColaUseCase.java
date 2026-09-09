package com.empresa.tomaturno.cola.application.command.usecase;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.port.output.ColaGatewayPort;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaNotFoundException;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;

public class ModificarColaUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaGatewayPort colaGatewayPort;

    public ModificarColaUseCase(ColaCommandRepository colaCommandRepository,
            ColaGatewayPort colaGatewayPort) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaGatewayPort = colaGatewayPort;
    }

    public Cola ejecutar(Long idCola, Long idSucursal, String nombre, String codigo, Estado estado,
            Auditoria auditoriaModificacion) {
        Cola cola = colaGatewayPort.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new ColaNotFoundException(idCola,
                    "Cola (idCola=" + idCola + ", idSucursal=" + idSucursal + ")");
        }
        cola.modificar(nombre, codigo, estado, auditoriaModificacion);
        return colaCommandRepository.modificar(cola);
    }
}
