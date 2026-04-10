package com.empresa.tomaturno.usuario.application.command.usecase;

import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ModificarUsuarioUseCase {

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioQueryRepository queryRepository;

    public ModificarUsuarioUseCase(UsuarioCommandRepository commandRepository,
                                    UsuarioQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Usuario ejecutar(Long idUsuario, Long idSucursal, Usuario datosNuevos, String user) {
        Usuario usuario = queryRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException(idUsuario,
                    "Usuario (idUsuario=" + idUsuario + ", idSucursal=" + idSucursal + ")");
        }

        String nuevaContrasena = datosNuevos.getContrasena();
        if (nuevaContrasena != null && !nuevaContrasena.isBlank()) {
            nuevaContrasena = BCrypt.withDefaults().hashToString(12, nuevaContrasena.toCharArray());
        }

        usuario.modificar(datosNuevos.getIdPuesto(), datosNuevos.getCodigoUsuario(), nuevaContrasena,
                datosNuevos.getEstado(), datosNuevos.getDatosPersonales(),
                datosNuevos.getConfiguracion(), user);
        return commandRepository.modificar(usuario);
    }
}
