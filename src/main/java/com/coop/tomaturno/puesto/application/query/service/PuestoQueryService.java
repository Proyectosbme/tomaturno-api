package com.coop.tomaturno.puesto.application.query.service;

import java.util.List;

import com.coop.tomaturno.puesto.application.query.port.input.PuestoQueryInputPort;
import com.coop.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.coop.tomaturno.puesto.application.query.usecase.BuscarPuestoPorFiltrosUseCase;
import com.coop.tomaturno.puesto.application.query.usecase.BuscarPuestoPorIdUseCase;
import com.coop.tomaturno.puesto.dominio.entity.Puesto;

public class PuestoQueryService implements PuestoQueryInputPort {

    private final BuscarPuestoPorFiltrosUseCase buscarPuestoPorFiltrosUseCase;
    private final BuscarPuestoPorIdUseCase buscarPuestoPorIdUseCase;

    public PuestoQueryService(PuestoQueryRepository puestoQueryRepository) {
        this.buscarPuestoPorFiltrosUseCase = new BuscarPuestoPorFiltrosUseCase(puestoQueryRepository);
        this.buscarPuestoPorIdUseCase = new BuscarPuestoPorIdUseCase(puestoQueryRepository);
    }

    @Override
    public List<Puesto> buscarPorFiltro(Long idSucursal, String nombre) {
        return buscarPuestoPorFiltrosUseCase.ejecutar(idSucursal, nombre);
    }

    @Override
    public Puesto buscarPorId(Long idPuesto, Long idSucursal) {
        return buscarPuestoPorIdUseCase.ejecutar(idPuesto, idSucursal);
    }
}
