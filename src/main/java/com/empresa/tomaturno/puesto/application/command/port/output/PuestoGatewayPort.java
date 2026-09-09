package com.empresa.tomaturno.puesto.application.command.port.output;

import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

/**
 * Lectura que el lado command necesita (resolver el puesto a modificar, validar unicidad
 * de nombre) sin depender del PuestoQueryRepository completo, que pertenece al lado query.
 */
public interface PuestoGatewayPort {
    Puesto buscarPorIdPuestoYSucursal(Long idPuesto, Long idSucursal);
    boolean existeNombreEnSucursal(Long idSucursal, String nombre);
}
