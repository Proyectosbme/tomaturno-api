package com.empresa.tomaturno.detallecolaxpuesto.application.command.port.input;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public interface DetalleColaxPuestoCommandInputPort {
    DetalleColaxPuesto asignar(DetalleColaxPuesto asignacion, String usuario);
    void desasignar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola);
}
