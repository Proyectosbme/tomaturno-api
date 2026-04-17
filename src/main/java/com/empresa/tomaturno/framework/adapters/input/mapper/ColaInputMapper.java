package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.cola.DTO.ResultadoReplicacion;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Sucursal;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ReplicarResponseDTO;
import com.empresa.tomaturno.shared.clases.Estado;

@Mapper(componentModel = "cdi")
public interface ColaInputMapper {

    default Cola toDomain(ColaRequestDTO dto) {
        return Cola.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .prioridad(dto.getPrioridad())
                .estado(Estado.fromCodigo(dto.getEstado()))
                .sucursal(new Sucursal(dto.getIdSucursal(), null))
                .inicializar();
    }

    default Detalle toDetalleDomain(DetalleRequestDTO dto) {
        return Detalle.inicializar(dto.getNombre(), dto.getCodigo(), Estado.fromCodigo(dto.getEstado()));
    }

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

    @Mapping(source = "correlativo", target = "idDetalle")
    @Mapping(source = "auditoria.usuarioCreacion", target = "usuarioCreacion")
    @Mapping(source = "auditoria.fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "auditoria.usuarioModificacion", target = "usuarioModificacion")
    @Mapping(source = "auditoria.fechaModificacion", target = "fechaModificacion")
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
