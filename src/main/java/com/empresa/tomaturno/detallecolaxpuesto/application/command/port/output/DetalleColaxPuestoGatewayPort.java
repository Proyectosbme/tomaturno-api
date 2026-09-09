package com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

/**
 * Lectura que el lado command necesita (validar existencia y resolver la asignación a
 * modificar) sin depender del DetalleColaxPuestoQueryRepository completo, que pertenece
 * al lado query.
 */
public interface DetalleColaxPuestoGatewayPort {

    boolean existeAsignacion(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola);

    DetalleColaxPuesto obtenerDetalleColaXPuesto(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle,
            Long idSucursalCola);
}
