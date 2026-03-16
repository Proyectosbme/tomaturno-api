package com.empresa.tomaturno.puesto.application.query.usecase;

import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.exceptions.PuestoNotFoundException;

public class BuscarPuestoPorIdUseCase {

    private final PuestoQueryRepository puestoQueryRepository;

    public BuscarPuestoPorIdUseCase(PuestoQueryRepository puestoQueryRepository) {
        this.puestoQueryRepository = puestoQueryRepository;
    }

    public Puesto ejecutar(Long idPuesto, Long idSucursal) {
        Puesto puesto = puestoQueryRepository.buscarPorIdPuestoYSucursal(idPuesto, idSucursal);
        if (puesto == null) {
            throw new PuestoNotFoundException(idPuesto,
                    "Puesto (idPuesto=" + idPuesto + ", idSucursal=" + idSucursal + ")");
        }
        return puesto;
    }
}
