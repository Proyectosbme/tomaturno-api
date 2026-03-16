package com.coop.tomaturno.detallecolaxpuesto.application.command.port.output;

import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public interface DetalleColaxPuestoCommandRepository {
    DetalleColaxPuesto save(DetalleColaxPuesto asignacion);
    void eliminar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola);
}
