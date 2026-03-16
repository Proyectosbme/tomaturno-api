package com.empresa.tomaturno.usuario.application.command.port.output;

import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public interface UsuarioCommandRepository {
    Usuario save(Usuario usuario);
    Usuario modificar(Usuario usuario);
}
