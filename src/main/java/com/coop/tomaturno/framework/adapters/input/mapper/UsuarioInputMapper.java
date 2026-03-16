package com.coop.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.coop.tomaturno.framework.adapters.input.dto.UsuarioRequestDTO;
import com.coop.tomaturno.framework.adapters.input.dto.UsuarioResponseDTO;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;
import com.coop.tomaturno.usuario.dominio.vo.Estado;

@Mapper(componentModel = "cdi")
public interface UsuarioInputMapper {

    @Mapping(ignore = true, target = "identificador")
    @Mapping(ignore = true, target = "auditoria")
    @Mapping(ignore = true, target = "nombreSucursal")
    @Mapping(ignore = true, target = "nombrePuesto")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "codigoToEstado")
    Usuario toDomain(UsuarioRequestDTO dto);

    @Mapping(source = "identificador", target = "id")
    @Mapping(source = "nombreSucursal", target = "nombreSucursal")
    @Mapping(source = "nombrePuesto", target = "nombrePuesto")
    @Mapping(source = "auditoria.usuarioCreacion", target = "usuarioCreacion")
    @Mapping(source = "auditoria.fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "auditoria.usuarioModificacion", target = "usuarioModificacion")
    @Mapping(source = "auditoria.fechaModificacion", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    UsuarioResponseDTO toResponse(Usuario usuario);

    @Named("codigoToEstado")
    default Estado codigoToEstado(Integer codigo) {
        return codigo != null ? Estado.fromCodigo(codigo) : null;
    }

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
