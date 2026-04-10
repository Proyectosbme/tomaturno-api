package com.empresa.tomaturno.usuario.application.command.usecase;

import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;

public class AsignarFotoUseCase {

    private final UsuarioCommandRepository usuarioCommandRepository;
    private final UsuarioQueryRepository usuarioQueryRepository;

    public AsignarFotoUseCase(UsuarioCommandRepository usuarioCommandRepository,
            UsuarioQueryRepository usuarioQueryRepository) {
        this.usuarioCommandRepository = usuarioCommandRepository;
        this.usuarioQueryRepository = usuarioQueryRepository;
    }

    public Usuario ejecutar(Long idUsuario, Long idSucursal, byte[] foto) {
        Usuario usuario = usuarioQueryRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException("No se encontró usuario con id: " + idUsuario + " en sucursal: " + idSucursal);
        }
        usuario.asignarFoto(foto);
        return usuarioCommandRepository.modificar(usuario);
    }
}
