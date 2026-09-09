package com.empresa.tomaturno.detallecolaxpuesto.application.query.service;

import java.util.List;

import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.input.DetalleColaxPuestoQueryInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.usecase.BuscarDetalleColaxPuestoPorColaUseCase;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.usecase.ListarDetalleColaxPuestoPorPuestoUseCase;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public class DetalleColaxPuestoQueryService implements DetalleColaxPuestoQueryInputPort {

    private final ListarDetalleColaxPuestoPorPuestoUseCase listarUseCase;
    private final BuscarDetalleColaxPuestoPorColaUseCase buscarPorColaUseCase;

    public DetalleColaxPuestoQueryService(DetalleColaxPuestoQueryRepository queryRepository) {
        this.listarUseCase = new ListarDetalleColaxPuestoPorPuestoUseCase(queryRepository);
        this.buscarPorColaUseCase = new BuscarDetalleColaxPuestoPorColaUseCase(queryRepository);
    }

    @Override
    public List<DetalleColaxPuesto> listarPorPuesto(Long idPuesto, Long idSucursalPuesto) {
        return listarUseCase.ejecutar(idPuesto, idSucursalPuesto);
    }

    @Override
    public List<DetalleColaxPuesto> buscarPorCola(Long idCola, Long idDetalle, Long idSucursalCola) {
        return buscarPorColaUseCase.ejecutar(idCola, idDetalle, idSucursalCola);
    }
}
