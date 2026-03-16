package com.coop.tomaturno.detallecolaxpuesto.application.command.port.input;

import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public interface DetalleColaxPuestoCommandInputPort {
    DetalleColaxPuesto asignar(DetalleColaxPuesto asignacion);
    void desasignar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola);
}
