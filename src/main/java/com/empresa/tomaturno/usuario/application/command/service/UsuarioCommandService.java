package com.empresa.tomaturno.usuario.application.command.service;

import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.usecase.AsignarFotoUseCase;
import com.empresa.tomaturno.usuario.application.command.usecase.CrearUsuarioUseCase;
import com.empresa.tomaturno.usuario.application.command.usecase.ModificarUsuarioUseCase;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public class UsuarioCommandService implements UsuarioCommandInputPort {

    private final CrearUsuarioUseCase crearUseCase;
    private final ModificarUsuarioUseCase modificarUseCase;
    private final AsignarFotoUseCase asignarFotoUseCase;

    public UsuarioCommandService(UsuarioCommandRepository commandRepository,
                                  UsuarioQueryRepository queryRepository, KeycloakAdminPort keycloakAdmin) {
        this.crearUseCase = new CrearUsuarioUseCase(commandRepository, queryRepository, keycloakAdmin);
        this.modificarUseCase = new ModificarUsuarioUseCase(commandRepository, queryRepository);
        this.asignarFotoUseCase = new AsignarFotoUseCase(commandRepository, queryRepository);
    }

    @Override
    public Usuario crear(Usuario usuario, String usuarioCreador) {
        return crearUseCase.ejecutar(usuario, usuarioCreador);
    }

    @Override
    public Usuario actualizar(Long idUsuario, Long idSucursal, Usuario datosActualizados, String usuarioActualizador) {
        return modificarUseCase.ejecutar(idUsuario, idSucursal, datosActualizados, usuarioActualizador);
    }

    @Override
    public Usuario asignarFoto(Long idUsuario, Long idSucursal, byte[] foto) {
        return asignarFotoUseCase.ejecutar(idUsuario, idSucursal, foto);
    }
}
