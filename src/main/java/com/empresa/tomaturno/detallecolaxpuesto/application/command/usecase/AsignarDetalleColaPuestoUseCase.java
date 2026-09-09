package com.empresa.tomaturno.detallecolaxpuesto.application.command.usecase;

import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoGatewayPort;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.exceptions.DetalleColaxPuestoValidationException;

public class AsignarDetalleColaPuestoUseCase {

    private final DetalleColaxPuestoCommandRepository commandRepository;
    private final DetalleColaxPuestoGatewayPort gatewayPort;

    public AsignarDetalleColaPuestoUseCase(DetalleColaxPuestoCommandRepository commandRepository,
                                            DetalleColaxPuestoGatewayPort gatewayPort) {
        this.commandRepository = commandRepository;
        this.gatewayPort = gatewayPort;
    }

    public DetalleColaxPuesto ejecutar(DetalleColaxPuesto asignacion) {
        boolean existe = gatewayPort.existeAsignacion(
                asignacion.getIdPuesto(), asignacion.getIdSucursalPuesto(),
                asignacion.getIdCola(), asignacion.getIdDetalle(), asignacion.getIdSucursalCola());
        if (existe) {
            throw new DetalleColaxPuestoValidationException(
                    "Ya existe la asignación de este detalle de cola al puesto indicado");
        }
        return commandRepository.save(asignacion);
    }
}
