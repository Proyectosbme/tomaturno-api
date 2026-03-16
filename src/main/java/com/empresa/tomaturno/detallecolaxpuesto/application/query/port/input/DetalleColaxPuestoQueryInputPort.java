package com.empresa.tomaturno.detallecolaxpuesto.application.query.port.input;

import java.util.List;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public interface DetalleColaxPuestoQueryInputPort {
    List<DetalleColaxPuesto> listarPorPuesto(Long idPuesto, Long idSucursalPuesto);
}
