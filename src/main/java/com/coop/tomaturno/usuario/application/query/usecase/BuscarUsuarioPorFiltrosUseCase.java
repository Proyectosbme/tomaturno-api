package com.coop.tomaturno.usuario.application.query.usecase;

import java.util.List;

import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;

public class BuscarUsuarioPorFiltrosUseCase {

    private final UsuarioQueryRepository queryRepository;

    public BuscarUsuarioPorFiltrosUseCase(UsuarioQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Usuario> ejecutar(Long idSucursal, String codigoUsuario, String nombre) {
        return queryRepository.buscarPorFiltro(idSucursal, codigoUsuario, nombre);
    }
}
