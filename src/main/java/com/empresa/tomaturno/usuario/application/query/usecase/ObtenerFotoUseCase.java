package com.empresa.tomaturno.usuario.application.query.usecase;

import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;

public class ObtenerFotoUseCase {

    private final UsuarioQueryRepository queryRepository;

    public ObtenerFotoUseCase(UsuarioQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public byte[] ejecutar(Long idUsuario, Long idSucursal) {
        Usuario usuario = queryRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException(idUsuario,
                    "Usuario (idUsuario=" + idUsuario + ", idSucursal=" + idSucursal + ")");
        }
        return usuario.getFoto();
    }
}
