package com.coop.tomaturno.detallecolaxpuesto.application.query.port.output;

import java.util.List;

import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public interface DetalleColaxPuestoQueryRepository {
    List<DetalleColaxPuesto> buscarPorPuesto(Long idPuesto, Long idSucursalPuesto);
    boolean existeAsignacion(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola);
}
