package com.coop.tomaturno.usuario.application.query.service;

import java.util.List;

import com.coop.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.application.query.usecase.BuscarUsuarioPorCodigoUseCase;
import com.coop.tomaturno.usuario.application.query.usecase.BuscarUsuarioPorFiltrosUseCase;
import com.coop.tomaturno.usuario.application.query.usecase.BuscarUsuarioPorIdUseCase;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;

public class UsuarioQueryService implements UsuarioQueryInputPort {

    private final BuscarUsuarioPorFiltrosUseCase buscarPorFiltrosUseCase;
    private final BuscarUsuarioPorIdUseCase buscarPorIdUseCase;
    private final BuscarUsuarioPorCodigoUseCase buscarPorCodigoUseCase;

    public UsuarioQueryService(UsuarioQueryRepository queryRepository) {
        this.buscarPorFiltrosUseCase = new BuscarUsuarioPorFiltrosUseCase(queryRepository);
        this.buscarPorIdUseCase = new BuscarUsuarioPorIdUseCase(queryRepository);
        this.buscarPorCodigoUseCase = new BuscarUsuarioPorCodigoUseCase(queryRepository);
    }

    @Override
    public List<Usuario> buscarPorFiltro(Long idSucursal, String codigoUsuario, String nombre) {
        return buscarPorFiltrosUseCase.ejecutar(idSucursal, codigoUsuario, nombre);
    }

    @Override
    public Usuario buscarPorId(Long idUsuario, Long idSucursal) {
        return buscarPorIdUseCase.ejecutar(idUsuario, idSucursal);
    }

    @Override
    public Usuario buscarPorCodigo(String codigoUsuario) {
        return buscarPorCodigoUseCase.ejecutar(codigoUsuario);
    }

    @Override
    public Usuario buscarPorCodigoYSucursal(String codigoUsuario, Long idSucursal) {
        return buscarPorCodigoUseCase.ejecutar(codigoUsuario, idSucursal);
    }
}
