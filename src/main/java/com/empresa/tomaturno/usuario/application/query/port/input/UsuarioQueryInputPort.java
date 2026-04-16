package com.empresa.tomaturno.usuario.application.query.port.input;

import java.util.List;

import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

public interface UsuarioQueryInputPort {
    List<Usuario> buscarPorFiltro(Long idSucursal, String codigoUsuario);
    Usuario buscarPorId(Long idUsuario, Long idSucursal);
    Usuario buscarPorCodigo(String codigoUsuario);
    Usuario buscarPorCodigoYSucursal(String codigoUsuario, Long idSucursal);
    byte[] obtenerFoto(Long idUsuario, Long idSucursal);
}
