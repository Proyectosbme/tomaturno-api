package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.empresa.tomaturno.framework.adapters.output.mapper.UsuarioOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.PuestoJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioJpaRepository;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioQueryJpaAdapters implements UsuarioQueryRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioOutputMapper usuarioOutputMapper;
    private final SucursalJpaRepository sucursalJpaRepository;
    private final PuestoJpaRepository puestoJpaRepository;

    public UsuarioQueryJpaAdapters(UsuarioJpaRepository usuarioJpaRepository,
                                    UsuarioOutputMapper usuarioOutputMapper,
                                    SucursalJpaRepository sucursalJpaRepository,
                                    PuestoJpaRepository puestoJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioOutputMapper = usuarioOutputMapper;
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.puestoJpaRepository = puestoJpaRepository;
    }

    @Override
    public Usuario buscarPorIdUsuarioYSucursal(Long idUsuario, Long idSucursal) {
        UsuarioJpaEntity entity = usuarioJpaRepository.buscarPorIdUsuarioYSucursal(idUsuario, idSucursal);
        if (entity == null) return null;
        Usuario usuario = usuarioOutputMapper.toDomain(entity);
        enriquecerConNombres(List.of(usuario));
        return usuario;
    }

    @Override
    public List<Usuario> buscarPorFiltro(Long idSucursal, String codigoUsuario, String nombre) {
        List<UsuarioJpaEntity> entities = usuarioJpaRepository.buscarPorFiltros(idSucursal, codigoUsuario, nombre);
        if (entities.isEmpty()) return List.of();

        List<Usuario> usuarios = entities.stream()
                .map(usuarioOutputMapper::toDomain)
                .toList();

        enriquecerConNombres(usuarios);
        return usuarios;
    }

    @Override
    public boolean existeCodigoEnSucursal(Long idSucursal, String codigoUsuario) {
        return usuarioJpaRepository.existeCodigoEnSucursal(idSucursal, codigoUsuario);
    }

    @Override
    public Usuario buscarPorCodigo(String codigoUsuario) {
        UsuarioJpaEntity entity = usuarioJpaRepository.buscarPorCodigo(codigoUsuario);
        if (entity == null) return null;
        Usuario usuario = usuarioOutputMapper.toDomain(entity);
        enriquecerConNombres(List.of(usuario));
        return usuario;
    }

    @Override
    public Usuario buscarPorCodigoYSucursal(String codigoUsuario, Long idSucursal) {
        UsuarioJpaEntity entity = usuarioJpaRepository.buscarPorCodigoYSucursal(codigoUsuario, idSucursal);
        if (entity == null) return null;
        Usuario usuario = usuarioOutputMapper.toDomain(entity);
        enriquecerConNombres(List.of(usuario));
        return usuario;
    }

    private void enriquecerConNombres(List<Usuario> usuarios) {
        List<Long> idsSucursales = usuarios.stream()
                .map(Usuario::getIdSucursal).distinct().toList();

        Map<Long, String> nombresSucursales = sucursalJpaRepository
                .find("id in ?1", idsSucursales).stream()
                .collect(Collectors.toMap(SucursalJpaEntity::getId, SucursalJpaEntity::getNombre));

        Map<Long, String> nombresPuestos = usuarios.stream()
                .filter(u -> u.getIdPuesto() != null && u.getIdSucursal() != null)
                .collect(Collectors.toMap(
                        u -> u.getIdPuesto() * 10000L + u.getIdSucursal(),
                        u -> {
                            PuestoJpaEntity p = puestoJpaRepository
                                    .buscarPorIdPuestoYSucursal(u.getIdPuesto(), u.getIdSucursal());
                            return p != null ? p.getNombre() : "";
                        },
                        (a, b) -> a));

        usuarios.forEach(u -> {
            u.enriquecerNombreSucursal(nombresSucursales.getOrDefault(u.getIdSucursal(), ""));
            if (u.getIdPuesto() != null) {
                u.enriquecerNombrePuesto(nombresPuestos.getOrDefault(
                        u.getIdPuesto() * 10000L + u.getIdSucursal(), ""));
            }
        });
    }
}
