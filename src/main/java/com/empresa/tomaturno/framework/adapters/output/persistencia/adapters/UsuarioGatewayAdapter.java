package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioGatewayPort;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public class UsuarioGatewayAdapter implements UsuarioGatewayPort {

    private final UsuarioQueryRepository usuarioQueryRepository;

    public UsuarioGatewayAdapter(UsuarioQueryRepository usuarioQueryRepository) {
        this.usuarioQueryRepository = usuarioQueryRepository;
    }

    @Override
    public Usuario buscarPorIdUsuarioYSucursal(Long idUsuario, Long idSucursal) {
        return usuarioQueryRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
    }

    @Override
    public String generaCodigoUsuario(String codigoUsuario) {
        return usuarioQueryRepository.generaCodigoUsuario(codigoUsuario);
    }
}
