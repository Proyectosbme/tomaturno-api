package com.empresa.tomaturno.puesto.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public class CrearPuestoUseCase {

    private final PuestoCommandRepository puestoCommandRepository;
    private final PuestoQueryRepository puestoQueryRepository;

    public CrearPuestoUseCase(PuestoCommandRepository puestoCommandRepository,
            PuestoQueryRepository puestoQueryRepository) {
        this.puestoCommandRepository = puestoCommandRepository;
        this.puestoQueryRepository = puestoQueryRepository;
    }

    public Puesto ejecutar(Puesto puesto) {
        puesto.auditoriaCreacion("bmarroquin", LocalDateTime.now());
        puesto.validarCreacion();

        boolean existeNombre = puestoQueryRepository.existeNombreEnSucursal(
                puesto.getSucursal().getIdentificador(), puesto.getNombre());
        puesto.validarNombreUnico(existeNombre);

        return puestoCommandRepository.save(puesto);
    }
}
