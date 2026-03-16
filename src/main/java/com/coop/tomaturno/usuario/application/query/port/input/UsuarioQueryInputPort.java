package com.coop.tomaturno.usuario.application.query.port.input;

import java.util.List;

import com.coop.tomaturno.usuario.dominio.entity.Usuario;

public interface UsuarioQueryInputPort {
    List<Usuario> buscarPorFiltro(Long idSucursal, String codigoUsuario, String nombre);
    Usuario buscarPorId(Long idUsuario, Long idSucursal);
    Usuario buscarPorCodigo(String codigoUsuario);
    Usuario buscarPorCodigoYSucursal(String codigoUsuario, Long idSucursal);
}
