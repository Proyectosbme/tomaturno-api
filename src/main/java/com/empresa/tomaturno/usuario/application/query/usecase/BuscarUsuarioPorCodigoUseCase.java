package com.empresa.tomaturno.usuario.application.query.usecase;

import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public class BuscarUsuarioPorCodigoUseCase {

    private final UsuarioQueryRepository queryRepository;

    public BuscarUsuarioPorCodigoUseCase(UsuarioQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Usuario ejecutar(String codigoUsuario) {
        return queryRepository.buscarPorCodigo(codigoUsuario);
    }

    public Usuario ejecutar(String codigoUsuario, Long idSucursal) {
        return queryRepository.buscarPorCodigoYSucursal(codigoUsuario, idSucursal);
    }
}
