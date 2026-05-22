package com.empresa.tomaturno.usuario.application.command.usecase;

import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;

public class ModificarUsuarioUseCase {

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioQueryRepository queryRepository;
     private final KeycloakAdminPort keycloakAdmin;

    public ModificarUsuarioUseCase(UsuarioCommandRepository commandRepository,
            UsuarioQueryRepository queryRepository,
         KeycloakAdminPort keycloakAdmin) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
        this.keycloakAdmin=keycloakAdmin;
    }

    public Usuario ejecutar(Long idUsuario, Long idSucursal, Usuario datosNuevos, String user) {
        Usuario usuario = queryRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException(idUsuario,
                    "Usuario (idUsuario=" + idUsuario + ", idSucursal=" + idSucursal + ")");
        }

        usuario.modificar(datosNuevos.getIdPuesto(),
                datosNuevos.getEstado(), datosNuevos.getDatosPersonales(),
                datosNuevos.getConfiguracion(), user);

        keycloakAdmin.actualizarUsuario(new CrearUsuarioKeycloakCommand(
                usuario.getCodigoUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                datosNuevos.getContrasena(),
                usuario.getPerfil(),
                idSucursal));

        return commandRepository.modificar(usuario);
    }
}
