package com.coop.tomaturno.usuario.application.command.usecase;

import java.time.LocalDateTime;

import com.coop.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;
import com.coop.tomaturno.usuario.dominio.exceptions.UsuarioNotFoundException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ModificarUsuarioUseCase {

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioQueryRepository queryRepository;

    public ModificarUsuarioUseCase(UsuarioCommandRepository commandRepository,
                                    UsuarioQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Usuario ejecutar(Long idUsuario, Long idSucursal, Usuario datosNuevos) {
        Usuario usuario = queryRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (usuario == null) {
            throw new UsuarioNotFoundException(idUsuario,
                    "Usuario (idUsuario=" + idUsuario + ", idSucursal=" + idSucursal + ")");
        }

        String nuevaContrasena = datosNuevos.getContrasena();
        if (nuevaContrasena != null && !nuevaContrasena.isBlank()) {
            nuevaContrasena = BCrypt.withDefaults().hashToString(12, nuevaContrasena.toCharArray());
        }

        usuario.modificar(datosNuevos.getCodigoUsuario(), nuevaContrasena, datosNuevos.getIdPuesto(),
                datosNuevos.getNombres(), datosNuevos.getApellidos(), datosNuevos.getDui(),
                datosNuevos.getEstado(), datosNuevos.getTelefono(), datosNuevos.getIp(),
                datosNuevos.getPerfil(), datosNuevos.getCorrelativo(), datosNuevos.getAtenderCasosEspeciales());

        usuario.auditoriaModificacion("bmarroquin", LocalDateTime.now());
        usuario.validarModificacion();
        return commandRepository.modificar(usuario);
    }
}
