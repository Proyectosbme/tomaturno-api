package com.coop.tomaturno.detallecolaxpuesto.application.query.port.input;

import java.util.List;

import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public interface DetalleColaxPuestoQueryInputPort {
    List<DetalleColaxPuesto> listarPorPuesto(Long idPuesto, Long idSucursalPuesto);
}
