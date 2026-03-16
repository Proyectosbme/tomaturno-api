package com.empresa.tomaturno.detallecolaxpuesto.application.query.service;

import java.util.List;

import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.input.DetalleColaxPuestoQueryInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.usecase.ListarDetalleColaxPuestoPorPuestoUseCase;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

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
