package com.empresa.tomaturno.usuario.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioGatewayPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;
import com.empresa.tomaturno.usuario.dominio.vo.Auditoria;

public class AsignarFotoUseCase {

    private static final String USUARIO_SISTEMA = "SISTEMA";

    private final UsuarioCommandRepository usuarioCommandRepository;
    private final UsuarioGatewayPort usuarioGatewayPort;

    public AsignarFotoUseCase(UsuarioCommandRepository usuarioCommandRepository,
            UsuarioGatewayPort usuarioGatewayPort) {
        this.usuarioCommandRepository = usuarioCommandRepository;
        this.usuarioGatewayPort = usuarioGatewayPort;
    }

    public Usuario ejecutar(Long idUsuario, Long idSucursal, byte[] foto) {
        Usuario usuario = usuarioGatewayPort.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException("No se encontró usuario con id: " + idUsuario + " en sucursal: " + idSucursal);
        }
        usuario.asignarFoto(foto, Auditoria.of(USUARIO_SISTEMA, LocalDateTime.now()));
        return usuarioCommandRepository.modificar(usuario);
    }
}
