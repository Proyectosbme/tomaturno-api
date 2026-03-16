package com.empresa.tomaturno.puesto.application.query.usecase;

import java.util.List;

import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public class BuscarPuestoPorFiltrosUseCase {

    private final PuestoQueryRepository puestoQueryRepository;

    public BuscarPuestoPorFiltrosUseCase(PuestoQueryRepository puestoQueryRepository) {
        this.puestoQueryRepository = puestoQueryRepository;
    }

    public List<Puesto> ejecutar(Long idSucursal, String nombre) {
        return puestoQueryRepository.buscarPorFiltro(idSucursal, nombre);
    }
}
