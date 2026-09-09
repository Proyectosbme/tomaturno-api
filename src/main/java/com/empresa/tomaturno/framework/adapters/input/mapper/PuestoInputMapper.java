package com.empresa.tomaturno.framework.adapters.input.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.input.dto.PuestoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.PuestoResponseDTO;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.puesto.dominio.vo.Estado;
import com.empresa.tomaturno.puesto.dominio.vo.Sucursal;

@Mapper(componentModel = "cdi")
public interface PuestoInputMapper {

    /** Puesto nuevo: la auditoría de creación se arma aquí con el usuario autenticado. */
    default Puesto toDomain(PuestoRequestDTO dto, String usuario) {
        return Puesto.of(new Puesto.Builder()
                .nombre(dto.getNombre())
                .nombreLlamada(dto.getNombreLlamada())
                .estado(Estado.fromCodigo(dto.getEstado()))
                .sucursal(new Sucursal(dto.getIdSucursal(), null))
                .auditoriaCreacion(Auditoria.of(usuario, LocalDateTime.now())));
    }

    @Mapping(source = "identificador", target = "id")
    @Mapping(source = "sucursal.identificador", target = "idSucursal")
    @Mapping(source = "sucursal.nombre", target = "nombreSucursal")
    @Mapping(source = "nombreLlamada", target = "nombreLlamada")
    @Mapping(source = "auditoriaCreacion.usuario", target = "usuarioCreacion")
    @Mapping(source = "auditoriaCreacion.fecha", target = "fechaCreacion")
    @Mapping(source = "auditoriaModificacion.usuario", target = "usuarioModificacion")
    @Mapping(source = "auditoriaModificacion.fecha", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    PuestoResponseDTO toResponse(Puesto puesto);

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
