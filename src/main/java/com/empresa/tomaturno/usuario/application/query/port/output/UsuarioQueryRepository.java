package com.empresa.tomaturno.usuario.application.query.port.output;

import java.util.List;

import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public interface UsuarioQueryRepository {
    Usuario buscarPorIdUsuarioYSucursal(Long idUsuario, Long idSucursal);
    List<Usuario> buscarPorFiltro(Long idSucursal, String codigoUsuario);
    List<Usuario> buscarPorPuesto(Long idPuesto, Long idSucursal);
    String generaCodigoUsuario(String codigoUsuario);
    Usuario buscarPorCodigo(String codigoUsuario);
    Usuario buscarPorCodigoYSucursal(String codigoUsuario, Long idSucursal);
    Usuario buscarPorDui(String dui);
}
