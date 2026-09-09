package com.empresa.tomaturno.cola.application.command.usecase;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.port.output.ColaGatewayPort;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaNotFoundException;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;

public class CrearDetalleDeColaUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaGatewayPort colaGatewayPort;

    public CrearDetalleDeColaUseCase(ColaCommandRepository colaCommandRepository,
            ColaGatewayPort colaGatewayPort) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaGatewayPort = colaGatewayPort;
    }

    public Cola ejecutar(Long idCola, Long idSucursal, Detalle.Builder detalleBuilder, Auditoria auditoriaCreacion) {
        // Agregado completo (con detalles) para validar unicidad sin otra consulta al repositorio
        Cola cola = colaGatewayPort.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new ColaNotFoundException(idCola, "Cola no encontrada");
        }
        Detalle detalle = cola.crearDetalle(detalleBuilder, auditoriaCreacion);
        return colaCommandRepository.guardarDetalle(idCola, idSucursal, detalle);
    }
}
