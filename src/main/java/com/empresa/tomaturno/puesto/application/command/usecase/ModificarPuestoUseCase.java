package com.empresa.tomaturno.puesto.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.exceptions.PuestoNotFoundException;

public class ModificarPuestoUseCase {

    private final PuestoCommandRepository puestoCommandRepository;
    private final PuestoQueryRepository puestoQueryRepository;

    public ModificarPuestoUseCase(PuestoCommandRepository puestoCommandRepository,
            PuestoQueryRepository puestoQueryRepository) {
        this.puestoCommandRepository = puestoCommandRepository;
        this.puestoQueryRepository = puestoQueryRepository;
    }

    public Puesto ejecutar(Long idPuesto, Long idSucursal, Puesto datosNuevos, String usuario) {
        Puesto puesto = puestoQueryRepository.buscarPorIdPuestoYSucursal(idPuesto, idSucursal);
        if (puesto == null) {
            throw new PuestoNotFoundException(idPuesto,
                    "Puesto (idPuesto=" + idPuesto + ", idSucursal=" + idSucursal + ")");
        }
        puesto.auditoriaModificacion(usuario, LocalDateTime.now());
        puesto.modificar(datosNuevos.getNombre(), datosNuevos.getNombreLlamada(), datosNuevos.getEstado());
        puesto.validarModificacion();
        return puestoCommandRepository.modificar(puesto);
    }
}
