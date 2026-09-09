package com.empresa.tomaturno.detallecolaxpuesto.application.command.usecase;

import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoGatewayPort;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.exceptions.DetalleColaxPuestoNotFoundException;

public class ModificarPrioridadUseCase {

    private final DetalleColaxPuestoCommandRepository commandRepository;
    private final DetalleColaxPuestoGatewayPort gatewayPort;

    public ModificarPrioridadUseCase(DetalleColaxPuestoCommandRepository commandRepository,
            DetalleColaxPuestoGatewayPort gatewayPort) {
        this.commandRepository = commandRepository;
        this.gatewayPort = gatewayPort;
    }

    public DetalleColaxPuesto ejecutar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola,Long prioridad) {
        DetalleColaxPuesto detalle = gatewayPort.obtenerDetalleColaXPuesto(idPuesto, idSucursalPuesto, idCola,
                idDetalle,
                idSucursalCola);
        if (detalle == null) {
            throw new DetalleColaxPuestoNotFoundException(
                    "No se encontró la asignación con los parámetros indicados");
        }
        detalle.actualizarPrioridad(prioridad.intValue());
       return commandRepository.modificarPrioridad(detalle);
    }
}
