package com.empresa.tomaturno.cola.application.command.port.output;

import java.util.List;

import com.empresa.tomaturno.cola.dominio.entity.Cola;

/**
 * Lecturas que el lado command necesita (para resolver la cola a modificar y traer las listas
 * que usan las especificaciones de unicidad) sin depender del ColaQueryRepository completo, que
 * pertenece al lado query.
 */
public interface ColaGatewayPort {

    Cola buscarPorIdColaYSucursal(Long idCola, Long idSucursal);

    Cola buscarConDetallesPorIdYSucursal(Long idCola, Long idSucursal);

    List<Cola> buscarConDetallesPorSucursal(Long idSucursal);
}
