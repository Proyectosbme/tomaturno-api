package com.coop.tomaturno.usuario.application.command.usecase;

import java.time.LocalDateTime;

import com.coop.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;
import com.coop.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CrearUsuarioUseCase {

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioQueryRepository queryRepository;

    public CrearUsuarioUseCase(UsuarioCommandRepository commandRepository,
                                UsuarioQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Usuario ejecutar(Usuario usuario) {
        usuario.auditoriaCreacion("bmarroquin", LocalDateTime.now());

        String creador = usuario.getPerfilCreador();
        if (creador != null && creador.equalsIgnoreCase("PUBLICO")
                && !"OPERADOR".equalsIgnoreCase(usuario.getPerfil())) {
            throw new UsuarioValidationException("El perfil PUBLICO solo puede crear usuarios de tipo OPERADOR");
        }

        boolean existeCodigo = queryRepository.existeCodigoEnSucursal(
                usuario.getIdSucursal(), usuario.getCodigoUsuario());
        usuario.validarCodigoUnico(existeCodigo);

        String hashContrasena = BCrypt.withDefaults().hashToString(12, usuario.getContrasena().toCharArray());
        usuario.setContrasena(hashContrasena);

        usuario.validarCreacion();
        return commandRepository.save(usuario);
    }
}
