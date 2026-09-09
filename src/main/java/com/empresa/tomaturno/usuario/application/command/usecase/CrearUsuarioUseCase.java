package com.empresa.tomaturno.usuario.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioGatewayPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.Auditoria;

public class CrearUsuarioUseCase {
    private final UsuarioCommandRepository commandRepository;
    private final UsuarioGatewayPort usuarioGatewayPort;
    private final KeycloakAdminPort keycloakAdmin;

    public CrearUsuarioUseCase(UsuarioCommandRepository commandRepository,
            UsuarioGatewayPort usuarioGatewayPort, KeycloakAdminPort keycloakAdmin) {
        this.commandRepository = commandRepository;
        this.usuarioGatewayPort = usuarioGatewayPort;
        this.keycloakAdmin = keycloakAdmin;
    }

    /**
     * builder llega parcial (sin código de usuario ni auditoría de creación): este
     * caso de uso resuelve ambos antes de validar/construir el agregado con
     * Usuario.of.
     */
    public Usuario ejecutar(Usuario.Builder builder, String usuarioCreador) {
        Usuario.validarPerfilCreador(builder.getPerfilCreador());

        String candidato = Usuario.generarCodigoUsuario(builder.getDatosPersonales());
        String codigoUnico = usuarioGatewayPort.generaCodigoUsuario(candidato);
        builder.codigoUsuario(codigoUnico);
        if (builder.getContrasena() == null || builder.getContrasena().isBlank()) {
            builder.contrasena(codigoUnico);
        }
        builder.auditoriaCreacion(Auditoria.of(usuarioCreador, LocalDateTime.now()));

        Usuario usuario = Usuario.of(builder);

        // Crear en Keycloak: nombres, apellidos, contraseña temporal, rol e idSucursal
        String keycloakId = keycloakAdmin.crearUsuario(this.crearDtokeycloak(usuario));
        usuario.asignarKeycloakId(keycloakId);
        return commandRepository.save(usuario);
    }

    private CrearUsuarioKeycloakCommand crearDtokeycloak(Usuario usuario) {
        return new CrearUsuarioKeycloakCommand(
                usuario.getCodigoUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getContrasena(),
                usuario.getPerfil(),
                usuario.getIdSucursal(),
                usuario.getCorreo(),
                true);
    }
}
