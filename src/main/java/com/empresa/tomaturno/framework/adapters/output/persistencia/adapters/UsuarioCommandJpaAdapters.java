package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.empresa.tomaturno.framework.adapters.output.mapper.UsuarioOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioJpaRepository;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioCommandJpaAdapters implements UsuarioCommandRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioOutputMapper usuarioOutputMapper;
    private final KeycloakAdminPort keycloakAdminPort;

    public UsuarioCommandJpaAdapters(UsuarioJpaRepository usuarioJpaRepository,
                                      UsuarioOutputMapper usuarioOutputMapper,
                                      KeycloakAdminPort keycloakAdminPort) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioOutputMapper = usuarioOutputMapper;
        this.keycloakAdminPort = keycloakAdminPort;
    }

    @Override
    public Usuario save(Usuario usuario) {
        Long nextId = usuarioJpaRepository.obtenerSiguienteId(usuario.getIdSucursal());
        usuario.asignarIdentificador(nextId);
        UsuarioJpaEntity entity = usuarioOutputMapper.toJpaEntity(usuario);
        usuarioJpaRepository.persist(entity);
        Usuario dominio = usuarioOutputMapper.toDomain(entity);
        keycloakAdminPort.enriquecerUsuarios(List.of(dominio));
        return dominio;
    }

    @Override
    public Usuario modificar(Usuario usuario) {
        UsuarioJpaEntity existente = usuarioJpaRepository
                .buscarPorIdUsuarioYSucursal(usuario.getIdentificador(), usuario.getIdSucursal());

        if (existente == null) {
            throw new NotFoundException("Usuario no encontrado con id: " + usuario.getIdentificador()
                    + " e idSucursal: " + usuario.getIdSucursal());
        }

        usuarioOutputMapper.updateEntityFromDomain(usuario, existente);
        usuarioJpaRepository.getEntityManager().merge(existente);
        Usuario dominio = usuarioOutputMapper.toDomain(existente);
        keycloakAdminPort.enriquecerUsuarios(List.of(dominio));
        return dominio;
    }
}
