package com.coop.tomaturno.detallecolaxpuesto.application.query.usecase;

import java.util.List;

import com.coop.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public class ListarDetalleColaxPuestoPorPuestoUseCase {

    private final DetalleColaxPuestoQueryRepository queryRepository;

    public ListarDetalleColaxPuestoPorPuestoUseCase(DetalleColaxPuestoQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<DetalleColaxPuesto> ejecutar(Long idPuesto, Long idSucursalPuesto) {
        return queryRepository.buscarPorPuesto(idPuesto, idSucursalPuesto);
    }
}
