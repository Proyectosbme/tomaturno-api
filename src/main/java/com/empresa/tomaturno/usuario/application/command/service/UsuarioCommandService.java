package com.empresa.tomaturno.usuario.application.command.service;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioGatewayPort;
import com.empresa.tomaturno.usuario.application.command.usecase.AsignarFotoUseCase;
import com.empresa.tomaturno.usuario.application.command.usecase.CrearUsuarioUseCase;
import com.empresa.tomaturno.usuario.application.command.usecase.ModificarUsuarioUseCase;
import com.empresa.tomaturno.usuario.application.command.usecase.RegistroUsuarioUseCase;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

public class UsuarioCommandService implements UsuarioCommandInputPort {

    private final CrearUsuarioUseCase crearUseCase;
    private final ModificarUsuarioUseCase modificarUseCase;
    private final AsignarFotoUseCase asignarFotoUseCase;
    private final RegistroUsuarioUseCase registroUsuarioUseCase;


    public UsuarioCommandService(UsuarioCommandRepository commandRepository,
                                  UsuarioGatewayPort usuarioGatewayPort,
                                KeycloakAdminPort keycloakAdmin ) {
        this.crearUseCase = new CrearUsuarioUseCase(commandRepository, usuarioGatewayPort, keycloakAdmin);
        this.modificarUseCase = new ModificarUsuarioUseCase(commandRepository, usuarioGatewayPort, keycloakAdmin);
        this.asignarFotoUseCase = new AsignarFotoUseCase(commandRepository, usuarioGatewayPort);
        this.registroUsuarioUseCase = new RegistroUsuarioUseCase(commandRepository, usuarioGatewayPort, keycloakAdmin);
    }

    @Override
    public Usuario crear(Usuario.Builder usuario, String usuarioCreador) {
        return crearUseCase.ejecutar(usuario, usuarioCreador);
    }

    @Override
    public Usuario actualizar(Long idUsuario, Long idSucursal, Long idPuesto, Estado estado,
            DatosPersonales datosPersonales, ConfiguracionOperador configuracion, String contrasena,
            String usuarioActualizador) {
        return modificarUseCase.ejecutar(idUsuario, idSucursal, idPuesto, estado, datosPersonales, configuracion,
                contrasena, usuarioActualizador);
    }

    @Override
    public Usuario asignarFoto(Long idUsuario, Long idSucursal, byte[] foto) {
        return asignarFotoUseCase.ejecutar(idUsuario, idSucursal, foto);
    }

    @Override
    public Usuario registro(Usuario.Builder usuario) {
        return registroUsuarioUseCase.ejecutar(usuario);
    }
}
