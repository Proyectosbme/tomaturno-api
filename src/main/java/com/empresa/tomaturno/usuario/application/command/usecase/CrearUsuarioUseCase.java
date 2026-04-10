package com.empresa.tomaturno.usuario.application.command.usecase;

import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CrearUsuarioUseCase {

    private static final String PERFIL_ADMIN = "ADMIN";
    private static final String PERFIL_SUBADMIN = "SUBADMIN";

    private final UsuarioCommandRepository commandRepository;
    private final UsuarioQueryRepository queryRepository;

    public CrearUsuarioUseCase(UsuarioCommandRepository commandRepository,
                                UsuarioQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Usuario ejecutar(Usuario usuario, String usuarioCreador) {
        String creador = usuario.getPerfilCreador();
        boolean creadorAutorizado = creador != null
                && (creador.equalsIgnoreCase(PERFIL_ADMIN) || creador.equalsIgnoreCase(PERFIL_SUBADMIN));
        if (!creadorAutorizado) {
            throw new UsuarioValidationException("Solo usuarios con perfil ADMIN o SUBADMIN pueden crear usuarios");
        }
        if (PERFIL_ADMIN.equalsIgnoreCase(usuario.getPerfil()) && !PERFIL_ADMIN.equalsIgnoreCase(creador)) {
            throw new UsuarioValidationException("Solo un usuario ADMIN puede crear usuarios con perfil ADMIN");
        }

        boolean existeCodigo = queryRepository.existeCodigoEnSucursal(
                usuario.getIdSucursal(), usuario.getCodigoUsuario());
        usuario.validarCodigoUnico(existeCodigo);

        String hash = BCrypt.withDefaults().hashToString(12, usuario.getContrasena().toCharArray());
        usuario.asignarContrasenaHasheada(hash);

        usuario.crear(usuarioCreador);
        return commandRepository.save(usuario);
    }
}
