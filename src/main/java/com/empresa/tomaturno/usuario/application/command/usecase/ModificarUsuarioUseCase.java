package com.empresa.tomaturno.usuario.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioGatewayPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;
import com.empresa.tomaturno.usuario.dominio.vo.Auditoria;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

public class ModificarUsuarioUseCase {

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioGatewayPort usuarioGatewayPort;
    private final KeycloakAdminPort keycloakAdmin;

    public ModificarUsuarioUseCase(UsuarioCommandRepository commandRepository,
            UsuarioGatewayPort usuarioGatewayPort, KeycloakAdminPort keycloakAdmin) {
        this.commandRepository = commandRepository;
        this.usuarioGatewayPort = usuarioGatewayPort;
        this.keycloakAdmin = keycloakAdmin;
    }

    public Usuario ejecutar(Long idUsuario, Long idSucursal, Long idPuesto, Estado estado,
            DatosPersonales datosPersonales, ConfiguracionOperador configuracion, String contrasena, String user) {
        Usuario usuario = usuarioGatewayPort.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException(idUsuario,
                    "Usuario (idUsuario=" + idUsuario + ", idSucursal=" + idSucursal + ")");
        }

        Auditoria auditoriaModificacion = Auditoria.of(user, LocalDateTime.now());
        usuario.modificar(idPuesto, estado, datosPersonales, configuracion, auditoriaModificacion);

        keycloakAdmin.actualizarUsuario(new CrearUsuarioKeycloakCommand(
                usuario.getCodigoUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                contrasena,
                usuario.getPerfil(),
                idSucursal,
                usuario.getCorreo(),
                usuario.getEstado() == Estado.ACTIVO));

        return commandRepository.modificar(usuario);
    }
}
