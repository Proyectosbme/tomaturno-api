package com.empresa.tomaturno.usuario.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioGatewayPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.Auditoria;

public class RegistroUsuarioUseCase {

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioGatewayPort usuarioGatewayPort;
    private final KeycloakAdminPort keycloakAdmin;

    public RegistroUsuarioUseCase(UsuarioCommandRepository commandRepository,
            UsuarioGatewayPort usuarioGatewayPort, KeycloakAdminPort keycloakAdmin) {
        this.commandRepository = commandRepository;
        this.usuarioGatewayPort = usuarioGatewayPort;
        this.keycloakAdmin = keycloakAdmin;
    }

    /**
     * Autorregistro: no hay un usuario administrador autenticado que lo cree, así
     * que la auditoría de creación se audita con el propio código generado (igual
     * que hacía Usuario.asignarCodigoUsuario en su rama de fallback antes del
     * refactor).
     */
    public Usuario ejecutar(Usuario.Builder builder) {
        String candidato = Usuario.generarCodigoUsuario(builder.getDatosPersonales());
        String codigoUnico = usuarioGatewayPort.generaCodigoUsuario(candidato);
        builder.codigoUsuario(codigoUnico);
        if (builder.getContrasena() == null || builder.getContrasena().isBlank()) {
            builder.contrasena(codigoUnico);
        }
        builder.auditoriaCreacion(Auditoria.of(codigoUnico, LocalDateTime.now()));

        Usuario usuario = Usuario.of(builder);

        // Crear en Keycloak: nombres, apellidos, contraseña temporal, rol e idSucursal
        // Se usa `usuario` (pre-save) para nombres/apellidos/perfil porque save() reconstituye
        // desde la JPA entity que no persiste esos campos (están en tablas separadas).
        String keycloakId = keycloakAdmin.crearUsuario(new CrearUsuarioKeycloakCommand(
                usuario.getCodigoUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getContrasena(),
                usuario.getPerfil(),
                usuario.getIdSucursal(),
                usuario.getCorreo(),
                true));
        usuario.asignarKeycloakId(keycloakId);

        return commandRepository.save(usuario);
    }
}
