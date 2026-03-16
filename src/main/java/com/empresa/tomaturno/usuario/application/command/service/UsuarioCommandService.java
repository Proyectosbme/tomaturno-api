package com.coop.tomaturno.usuario.application.command.service;

import com.coop.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.coop.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.coop.tomaturno.usuario.application.command.usecase.CrearUsuarioUseCase;
import com.coop.tomaturno.usuario.application.command.usecase.ModificarUsuarioUseCase;
import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;

public class UsuarioCommandService implements UsuarioCommandInputPort {

    private final CrearUsuarioUseCase crearUseCase;
    private final ModificarUsuarioUseCase modificarUseCase;

    public UsuarioCommandService(UsuarioCommandRepository commandRepository,
                                  UsuarioQueryRepository queryRepository) {
        this.crearUseCase = new CrearUsuarioUseCase(commandRepository, queryRepository);
        this.modificarUseCase = new ModificarUsuarioUseCase(commandRepository, queryRepository);
    }

    @Override
    public Usuario crear(Usuario usuario) {
        return crearUseCase.ejecutar(usuario);
    }

    @Override
    public Usuario actualizar(Long idUsuario, Long idSucursal, Usuario datosActualizados) {
        return modificarUseCase.ejecutar(idUsuario, idSucursal, datosActualizados);
    }
}
