package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.input.dto.PuestoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.PuestoResponseDTO;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.vo.Estado;

@Mapper(componentModel = "cdi")
public interface PuestoInputMapper {

    @Mapping(ignore = true, target = "identificador")
    @Mapping(ignore = true, target = "auditoria")
    @Mapping(source = "idSucursal", target = "sucursal.identificador")
    @Mapping(ignore = true, target = "sucursal.nombre")
    @Mapping(source = "nombreLlamada", target = "nombreLlamada")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "codigoToEstado")
    Puesto toDomain(PuestoRequestDTO dto);

    @Mapping(source = "identificador", target = "id")
    @Mapping(source = "sucursal.identificador", target = "idSucursal")
    @Mapping(source = "sucursal.nombre", target = "nombreSucursal")
    @Mapping(source = "nombreLlamada", target = "nombreLlamada")
    @Mapping(source = "auditoria.usuarioCreacion", target = "usuarioCreacion")
    @Mapping(source = "auditoria.fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "auditoria.usuarioModificacion", target = "usuarioModificacion")
    @Mapping(source = "auditoria.fechaModificacion", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    PuestoResponseDTO toResponse(Puesto puesto);

    @Named("codigoToEstado")
    default Estado codigoToEstado(Integer codigo) {
        return codigo != null ? Estado.fromCodigo(codigo) : null;
    }

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
