package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioEstadoOperadorJpaEntity;
import com.empresa.tomaturno.shared.clases.Auditoria;

@Mapper(componentModel = "cdi")
public interface EstadoOperadorOutputMapper {

    // ─── Dominio → JPA ────────────────────────────────────────────────────

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    UsuarioEstadoOperadorJpaEntity toJpaEntity(EstadoOperador estadoOperador);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    void updateEntityFromDomain(EstadoOperador estadoOperador, @MappingTarget UsuarioEstadoOperadorJpaEntity entity);

    // ─── JPA → Dominio ────────────────────────────────────────────────────

    default EstadoOperador toDomain(UsuarioEstadoOperadorJpaEntity e) {
        if (e == null)
            return null;
        Auditoria auditoria = Auditoria.reconstituir(
                e.getUserCreacion(), e.getFechaCreacion(),
                e.getUserModificacion(), e.getFechaModificacion());
        return EstadoOperador.of(new EstadoOperador.Builder()
                .id(e.getId())
                .idUsuario(e.getIdUsuario())
                .idSucursal(e.getIdSucursal())
                .idPuesto(e.getIdPuesto())
                .idEstadoOperador(e.getIdEstadoOperador())
                .idTipoDescanso(e.getIdTipoDescanso())
                .comentario(e.getComentario())
                .fechaInicio(e.getFechaInicio())
                .fechaFin(e.getFechaFin())
                .auditoria(auditoria));
    }
}
