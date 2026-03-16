package com.coop.tomaturno.usuario.application.query.usecase;

import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;
import com.coop.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;

public class BuscarUsuarioPorIdUseCase {

    private final UsuarioQueryRepository queryRepository;

    public BuscarUsuarioPorIdUseCase(UsuarioQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Usuario ejecutar(Long idUsuario, Long idSucursal) {
        Usuario usuario = queryRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException(idUsuario,
                    "Usuario (idUsuario=" + idUsuario + ", idSucursal=" + idSucursal + ")");
        }
        return usuario;
    }
}
