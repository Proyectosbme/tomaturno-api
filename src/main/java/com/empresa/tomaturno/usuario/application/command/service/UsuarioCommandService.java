package com.empresa.tomaturno.usuario.application.command.service;

import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.usecase.CrearUsuarioUseCase;
import com.empresa.tomaturno.usuario.application.command.usecase.ModificarUsuarioUseCase;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public class UsuarioCommandService implements UsuarioCommandInputPort {

    private final CrearUsuarioUseCase crearUseCase;
    private final ModificarUsuarioUseCase modificarUseCase;

    public UsuarioCommandService(UsuarioCommandRepository commandRepository,
                                  UsuarioQueryRepository queryRepository) {
        this.crearUseCase = new CrearUsuarioUseCase(commandRepository, queryRepository);
        this.modificarUseCase = new ModificarUsuarioUseCase(commandRepository, queryRepository);
    }

    @Override
    public Usuario crear(Usuario usuario, String usuarioCreador) {
        return crearUseCase.ejecutar(usuario, usuarioCreador);
    }

    @Override
    public Usuario actualizar(Long idUsuario, Long idSucursal, Usuario datosActualizados, String usuarioActualizador) {
        return modificarUseCase.ejecutar(idUsuario, idSucursal, datosActualizados, usuarioActualizador);
    }
}
