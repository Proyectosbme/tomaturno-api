package com.empresa.tomaturno.estadooperador.application.command.port.output;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

/**
 * Lectura que el lado command necesita (el estado vigente del operador, para cerrarlo
 * antes de crear el nuevo) sin depender del EstadoOperadorQueryRepository completo, que
 * pertenece al lado query.
 */
public interface EstadoOperadorGatewayPort {

    EstadoOperador buscarVigente(Long idUsuario, Long idSucursal);
}
