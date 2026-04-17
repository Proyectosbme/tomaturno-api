package com.empresa.tomaturno.usuario.application.command.usecase;

import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public class RegistroUsuarioUseCase {

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioQueryRepository queryRepository;
    private final KeycloakAdminPort keycloakAdmin;

    public RegistroUsuarioUseCase(UsuarioCommandRepository commandRepository,
            UsuarioQueryRepository queryRepository, KeycloakAdminPort keycloakAdmin) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
        this.keycloakAdmin = keycloakAdmin;
    }

    public Usuario ejecutar(Usuario usuario) {        

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
        usuario.crear(usuario.getCodigoUsuario());

        // Crear en Keycloak: nombres, apellidos, contraseña temporal, rol e idSucursal
        // Se usa `usuario` (pre-save) para nombres/apellidos/perfil porque save() reconstituye
        // desde la JPA entity que no persiste esos campos (están en tablas separadas).
        String keycloakId = keycloakAdmin.crearUsuario(new CrearUsuarioKeycloakCommand(
                usuario.getCodigoUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                contrasena,
                usuario.getPerfil(),
                usuario.getIdSucursal()));
        usuario.asignarKeycloakId(keycloakId);

        
        return commandRepository.modificar(usuario);
    }
}
