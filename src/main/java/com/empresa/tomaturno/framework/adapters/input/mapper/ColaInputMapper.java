package com.empresa.tomaturno.framework.adapters.input.mapper;

import java.time.LocalDateTime;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.cola.application.command.dto.ResultadoReplicacion;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;
import com.empresa.tomaturno.cola.dominio.vo.Sucursal;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ReplicarResponseDTO;

@Mapper(componentModel = "cdi")
public interface ColaInputMapper {

    /** Cola nueva: la auditoría de creación se arma aquí con el usuario autenticado. */
    default Cola toDomain(ColaRequestDTO dto, String usuario) {
        return Cola.of(new Cola.Builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .estado(Estado.fromCodigo(dto.getEstado()))
                .sucursal(new Sucursal(dto.getIdSucursal(), null))
                .auditoriaCreacion(Auditoria.of(usuario, LocalDateTime.now())));
    }

    /** Detalle.crear es protected: se arma el Builder acá y se termina de construir vía Cola.crearDetalle. */
    default Detalle.Builder toDetalleBuilder(DetalleRequestDTO dto) {
        return new Detalle.Builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .estado(Estado.fromCodigo(dto.getEstado()));
    }

    @Mapping(source = "identificador", target = "id")
    @Mapping(source = "sucursal.identificador", target = "idSucursal")
    @Mapping(source = "sucursal.nombre", target = "nombreSucursal")
    @Mapping(source = "auditoriaCreacion.usuario", target = "usuarioCreacion")
    @Mapping(source = "auditoriaCreacion.fecha", target = "fechaCreacion")
    @Mapping(source = "auditoriaModificacion.usuario", target = "usuarioModificacion")
    @Mapping(source = "auditoriaModificacion.fecha", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "detalles", target = "detalles")
    ColaResponseDTO toResponse(Cola cola);

    @Mapping(source = "correlativo", target = "idDetalle")
    @Mapping(source = "auditoriaCreacion.usuario", target = "usuarioCreacion")
    @Mapping(source = "auditoriaCreacion.fecha", target = "fechaCreacion")
    @Mapping(source = "auditoriaModificacion.usuario", target = "usuarioModificacion")
    @Mapping(source = "auditoriaModificacion.fecha", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "idCola", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    DetalleResponseDTO toDetalleResponse(Detalle detalle);

    @AfterMapping
    default void setDetalleIds(@MappingTarget ColaResponseDTO response, Cola cola) {
        if (response.getDetalles() == null) return;
        Long idCola     = cola.getIdentificador();
        Long idSucursal = cola.getSucursal() != null ? cola.getSucursal().getIdentificador() : null;
        response.getDetalles().forEach(d -> {
            d.setIdCola(idCola);
            d.setIdSucursal(idSucursal);
        });
    }

    ReplicarResponseDTO toReplicarResponse(ResultadoReplicacion resultado);

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
