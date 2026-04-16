package com.empresa.tomaturno.usuario.application.query.usecase;

import java.util.List;

import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public class BuscarUsuarioPorFiltrosUseCase {

    private final UsuarioQueryRepository queryRepository;

    public BuscarUsuarioPorFiltrosUseCase(UsuarioQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Usuario> ejecutar(Long idSucursal, String codigoUsuario) {
        return queryRepository.buscarPorFiltro(idSucursal, codigoUsuario);
    }
}
