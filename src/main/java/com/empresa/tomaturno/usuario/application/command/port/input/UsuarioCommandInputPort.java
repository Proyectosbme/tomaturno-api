package com.empresa.tomaturno.usuario.application.command.port.input;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

public interface UsuarioCommandInputPort {
    Usuario registro(Usuario.Builder usuario);
    Usuario crear(Usuario.Builder usuario, String usuarioCreador);
    Usuario actualizar(Long idUsuario, Long idSucursal, Long idPuesto, Estado estado,
            DatosPersonales datosPersonales, ConfiguracionOperador configuracion, String contrasena,
            String usuarioActualizador);
    Usuario asignarFoto(Long idUsuario, Long idSucursal, byte[] foto);
}
