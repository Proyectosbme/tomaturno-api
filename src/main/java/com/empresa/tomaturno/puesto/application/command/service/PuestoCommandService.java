package com.empresa.tomaturno.puesto.application.command.service;

import com.empresa.tomaturno.puesto.application.command.port.input.PuestoCommandInputPort;
import com.empresa.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.empresa.tomaturno.puesto.application.command.usecase.CrearPuestoUseCase;
import com.empresa.tomaturno.puesto.application.command.usecase.ModificarPuestoUseCase;
import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public class PuestoCommandService implements PuestoCommandInputPort {

    private final CrearPuestoUseCase crearPuestoUseCase;
    private final ModificarPuestoUseCase modificarPuestoUseCase;

    public PuestoCommandService(PuestoCommandRepository puestoCommandRepository,
            PuestoQueryRepository puestoQueryRepository) {
        this.crearPuestoUseCase = new CrearPuestoUseCase(puestoCommandRepository, puestoQueryRepository);
        this.modificarPuestoUseCase = new ModificarPuestoUseCase(puestoCommandRepository, puestoQueryRepository);
    }

    @Override
    public Puesto crear(Puesto puesto) {
        return crearPuestoUseCase.ejecutar(puesto);
    }

    @Override
    public Puesto actualizar(Long idPuesto, Long idSucursal, Puesto datosActualizados) {
        return modificarPuestoUseCase.ejecutar(idPuesto, idSucursal, datosActualizados);
    }
}
