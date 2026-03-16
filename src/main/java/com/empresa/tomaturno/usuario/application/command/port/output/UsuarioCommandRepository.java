package com.coop.tomaturno.usuario.application.command.port.output;

import com.coop.tomaturno.usuario.dominio.entity.Usuario;

public interface UsuarioCommandRepository {
    Usuario save(Usuario usuario);
    Usuario modificar(Usuario usuario);
}
