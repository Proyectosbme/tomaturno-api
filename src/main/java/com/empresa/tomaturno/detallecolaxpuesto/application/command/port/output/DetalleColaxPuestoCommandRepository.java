package com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public interface DetalleColaxPuestoCommandRepository {
    DetalleColaxPuesto save(DetalleColaxPuesto asignacion);
    void eliminar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola);
}
