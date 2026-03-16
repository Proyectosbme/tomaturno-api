package com.coop.tomaturno.usuario.application.command.port.input;

import com.coop.tomaturno.usuario.dominio.entity.Usuario;

public interface UsuarioCommandInputPort {
    Usuario crear(Usuario usuario);
    Usuario actualizar(Long idUsuario, Long idSucursal, Usuario datosActualizados);
}
