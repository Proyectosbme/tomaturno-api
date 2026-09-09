package com.empresa.tomaturno.detallecolaxpuesto.application.query.usecase;

import java.util.List;

import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public class BuscarDetalleColaxPuestoPorColaUseCase {

    private final DetalleColaxPuestoQueryRepository queryRepository;

    public BuscarDetalleColaxPuestoPorColaUseCase(DetalleColaxPuestoQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<DetalleColaxPuesto> ejecutar(Long idCola, Long idDetalle, Long idSucursalCola) {
        return queryRepository.buscarPorCola(idCola, idDetalle, idSucursalCola);
    }
}
