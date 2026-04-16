package com.empresa.tomaturno.usuario.application.command.port.output;

import java.util.List;

import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public interface KeycloakAdminPort {

    /**
     * Crea un usuario en Keycloak con sus datos personales y rol.
     *
     * @return keycloakId (UUID del usuario creado en Keycloak)
     */
    String crearUsuario(CrearUsuarioKeycloakCommand command);

    List<Usuario> enriquecerUsuarios(List<Usuario> usuarios);
}
