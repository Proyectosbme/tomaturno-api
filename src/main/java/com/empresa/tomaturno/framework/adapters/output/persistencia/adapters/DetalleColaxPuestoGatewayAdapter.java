package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoGatewayPort;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public class DetalleColaxPuestoGatewayAdapter implements DetalleColaxPuestoGatewayPort {

    private final DetalleColaxPuestoQueryRepository detalleColaxPuestoQueryRepository;

    public DetalleColaxPuestoGatewayAdapter(DetalleColaxPuestoQueryRepository detalleColaxPuestoQueryRepository) {
        this.detalleColaxPuestoQueryRepository = detalleColaxPuestoQueryRepository;
    }

    @Override
    public boolean existeAsignacion(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle,
            Long idSucursalCola) {
        return detalleColaxPuestoQueryRepository.existeAsignacion(idPuesto, idSucursalPuesto, idCola, idDetalle,
                idSucursalCola);
    }

    @Override
    public DetalleColaxPuesto obtenerDetalleColaXPuesto(Long idPuesto, Long idSucursalPuesto, Long idCola,
            Long idDetalle, Long idSucursalCola) {
        return detalleColaxPuestoQueryRepository.obtenerDetalleColaXPuesto(idPuesto, idSucursalPuesto, idCola,
                idDetalle, idSucursalCola);
    }
}
