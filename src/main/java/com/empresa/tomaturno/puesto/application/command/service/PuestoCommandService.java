package com.empresa.tomaturno.puesto.application.command.service;

import com.empresa.tomaturno.puesto.application.command.port.input.PuestoCommandInputPort;
import com.empresa.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.empresa.tomaturno.puesto.application.command.port.output.PuestoGatewayPort;
import com.empresa.tomaturno.puesto.application.command.usecase.CrearPuestoUseCase;
import com.empresa.tomaturno.puesto.application.command.usecase.ModificarPuestoUseCase;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.puesto.dominio.vo.Estado;

public class PuestoCommandService implements PuestoCommandInputPort {

    private final CrearPuestoUseCase crearPuestoUseCase;
    private final ModificarPuestoUseCase modificarPuestoUseCase;

    public PuestoCommandService(PuestoCommandRepository puestoCommandRepository,
            PuestoGatewayPort puestoGatewayPort) {
        this.crearPuestoUseCase = new CrearPuestoUseCase(puestoCommandRepository, puestoGatewayPort);
        this.modificarPuestoUseCase = new ModificarPuestoUseCase(puestoCommandRepository, puestoGatewayPort);
    }

    @Override
    public Puesto crear(Puesto puesto) {
        return crearPuestoUseCase.ejecutar(puesto);
    }

    @Override
    public Puesto actualizar(Long idPuesto, Long idSucursal, String nombre, String nombreLlamada, Estado estado,
            Auditoria auditoriaModificacion) {
        return modificarPuestoUseCase.ejecutar(idPuesto, idSucursal, nombre, nombreLlamada, estado,
                auditoriaModificacion);
    }
}
