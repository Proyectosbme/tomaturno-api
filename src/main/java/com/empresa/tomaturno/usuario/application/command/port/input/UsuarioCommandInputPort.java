package com.empresa.tomaturno.usuario.application.command.port.input;

import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public interface UsuarioCommandInputPort {
    Usuario crear(Usuario usuario, String usuarioCreador);
    Usuario actualizar(Long idUsuario, Long idSucursal, Usuario datosActualizados, String usuarioActualizador);
}
