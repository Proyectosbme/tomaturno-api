package com.empresa.tomaturno.framework.adapters.input.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.cola.DTO.ResultadoReplicacion;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Estado;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ReplicarResponseDTO;

@Mapper(componentModel = "cdi")
public interface ColaInputMapper {

    @Mapping(ignore = true, target = "identificador")
    @Mapping(ignore = true, target = "auditoria")
    @Mapping(source = "idSucursal", target = "sucursal.identificador")
    @Mapping(ignore = true, target = "sucursal.nombre")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "codigoToEstado")
    Cola toDomain(ColaRequestDTO colaRequestDTO);

    @Mapping(source = "identificador", target = "id")
    @Mapping(source = "sucursal.identificador", target = "idSucursal")
    @Mapping(source = "sucursal.nombre", target = "nombreSucursal")
    @Mapping(source = "auditoria.usuarioCreacion", target = "usuarioCreacion")
    @Mapping(source = "auditoria.fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "auditoria.usuarioModificacion", target = "usuarioModificacion")
    @Mapping(source = "auditoria.fechaModificacion", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "detalles", target = "detalles")
    ColaResponseDTO toResponse(Cola cola);

    // Agrega este método para mapear cada detalle:
    @Mapping(source = "correlativo", target = "idDetalle")
    @Mapping(source = "auditoria.usuarioCreacion", target = "usuarioCreacion")
    @Mapping(source = "auditoria.fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "auditoria.usuarioModificacion", target = "usuarioModificacion")
    @Mapping(source = "auditoria.fechaModificacion", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    DetalleResponseDTO toDetalleResponse(Detalle detalle);

    @Mapping(source = "estado", target = "estado", qualifiedByName = "codigoToEstado")
    @Mapping(ignore = true, target = "correlativo")
    @Mapping(ignore = true, target = "auditoria")
    Detalle toDetalleDomain(DetalleRequestDTO dto);

    ReplicarResponseDTO toReplicarResponse(ResultadoReplicacion resultado);

    @Named("codigoToEstado")
    default Estado codigoToEstado(Integer codigo) {
        return codigo != null ? Estado.fromCodigo(codigo) : null;
    }

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }

}
