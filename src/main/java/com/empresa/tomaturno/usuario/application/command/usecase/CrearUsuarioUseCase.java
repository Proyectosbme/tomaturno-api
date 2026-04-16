package com.empresa.tomaturno.usuario.application.command.usecase;

import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;

public class CrearUsuarioUseCase {

    private static final String PERFIL_ADMIN = "ADMIN";
    private static final String PERFIL_SUBADMIN = "SUBADMIN";

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioQueryRepository queryRepository;
    private final KeycloakAdminPort keycloakAdmin;

    public CrearUsuarioUseCase(UsuarioCommandRepository commandRepository,
            UsuarioQueryRepository queryRepository, KeycloakAdminPort keycloakAdmin) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
        this.keycloakAdmin = keycloakAdmin;
    }

    public Usuario ejecutar(Usuario usuario, String usuarioCreador) {
        String creador = usuario.getPerfilCreador();
        boolean creadorAutorizado = creador != null
                && (creador.equalsIgnoreCase(PERFIL_ADMIN) || creador.equalsIgnoreCase(PERFIL_SUBADMIN));
        if (!creadorAutorizado) {
            throw new UsuarioValidationException("Solo usuarios con perfil ADMIN o SUBADMIN pueden crear usuarios");
        }
        if (PERFIL_ADMIN.equalsIgnoreCase(usuario.getPerfil()) && !PERFIL_ADMIN.equalsIgnoreCase(creador)) {
            throw new UsuarioValidationException("Solo un usuario ADMIN puede crear usuarios con perfil ADMIN");
        }

        if (usuario.getCodigoUsuario() == null || usuario.getCodigoUsuario().isBlank()) {
            usuario.crearCodigoUsuario();
        }

        String codigo = queryRepository.existeCodigoEnSucursal(
                usuario.getIdSucursal(), usuario.getCodigoUsuario());
        usuario.asignarCodigoUsuario(codigo);

        // Si no se envió contraseña, usar el código de usuario como contraseña temporal
        String contrasena = (usuario.getContrasena() != null && !usuario.getContrasena().isBlank())
                ? usuario.getContrasena()
                : usuario.getCodigoUsuario();

        // Crear en Keycloak: nombres, apellidos, contraseña temporal, rol e idSucursal
        String keycloakId = keycloakAdmin.crearUsuario(new CrearUsuarioKeycloakCommand(
                usuario.getCodigoUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                contrasena,
                usuario.getPerfil(),
                usuario.getIdSucursal()));
        usuario.asignarKeycloakId(keycloakId);

        usuario.crear(usuarioCreador);
        return commandRepository.save(usuario);
    }
}
