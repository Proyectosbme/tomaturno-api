package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.empresa.tomaturno.framework.adapters.output.mapper.UsuarioOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioJpaRepository;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioCommandJpaAdapters implements UsuarioCommandRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioOutputMapper usuarioOutputMapper;

    public UsuarioCommandJpaAdapters(UsuarioJpaRepository usuarioJpaRepository,
                                      UsuarioOutputMapper usuarioOutputMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioOutputMapper = usuarioOutputMapper;
    }

    @Override
    public Usuario save(Usuario usuario) {
        Long nextId = usuarioJpaRepository.obtenerSiguienteId(usuario.getIdSucursal());
        usuario.setIdentificador(nextId);
        UsuarioJpaEntity entity = usuarioOutputMapper.toJpaEntity(usuario);
        usuarioJpaRepository.persist(entity);
        return usuarioOutputMapper.toDomain(entity);
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
        return usuarioOutputMapper.toDomain(existente);
    }
}
