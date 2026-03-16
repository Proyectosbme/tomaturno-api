package com.coop.tomaturno.detallecolaxpuesto.application.query.service;

import java.util.List;

import com.coop.tomaturno.detallecolaxpuesto.application.query.port.input.DetalleColaxPuestoQueryInputPort;
import com.coop.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.coop.tomaturno.detallecolaxpuesto.application.query.usecase.ListarDetalleColaxPuestoPorPuestoUseCase;
import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public class DetalleColaxPuestoQueryService implements DetalleColaxPuestoQueryInputPort {

    private final ListarDetalleColaxPuestoPorPuestoUseCase listarUseCase;

    public DetalleColaxPuestoQueryService(DetalleColaxPuestoQueryRepository queryRepository) {
        this.listarUseCase = new ListarDetalleColaxPuestoPorPuestoUseCase(queryRepository);
    }

    @Override
    public List<DetalleColaxPuesto> listarPorPuesto(Long idPuesto, Long idSucursalPuesto) {
        return listarUseCase.ejecutar(idPuesto, idSucursalPuesto);
    }
}
