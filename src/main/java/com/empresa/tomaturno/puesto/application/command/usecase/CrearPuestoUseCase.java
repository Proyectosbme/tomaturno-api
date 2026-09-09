package com.empresa.tomaturno.puesto.application.command.usecase;

import com.empresa.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.empresa.tomaturno.puesto.application.command.port.output.PuestoGatewayPort;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public class CrearPuestoUseCase {

    private final PuestoCommandRepository puestoCommandRepository;
    private final PuestoGatewayPort puestoGatewayPort;

    public CrearPuestoUseCase(PuestoCommandRepository puestoCommandRepository,
            PuestoGatewayPort puestoGatewayPort) {
        this.puestoCommandRepository = puestoCommandRepository;
        this.puestoGatewayPort = puestoGatewayPort;
    }

    public Puesto ejecutar(Puesto puesto) {
        boolean existeNombre = puestoGatewayPort.existeNombreEnSucursal(
                puesto.getSucursal().getIdentificador(), puesto.getNombre());
        puesto.validarNombreUnico(existeNombre);

        return puestoCommandRepository.save(puesto);
    }
}
