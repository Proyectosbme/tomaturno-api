package com.empresa.tomaturno.usuario.application.command.port.output;

import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

/**
 * Lecturas que el lado command necesita (resolver el usuario a modificar, generar un
 * código de usuario único) sin depender del UsuarioQueryRepository completo, que
 * pertenece al lado query.
 */
public interface UsuarioGatewayPort {
    Usuario buscarPorIdUsuarioYSucursal(Long idUsuario, Long idSucursal);
    String generaCodigoUsuario(String codigoUsuario);
}
