package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.framework.adapters.input.dto.ConfiguracionRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ConfiguracionResponseDTO;
import com.empresa.tomaturno.shared.clases.Estado;

@Mapper(componentModel = "cdi")
public interface ConfiguracionInputMapper {

    default Configuracion toDomain(ConfiguracionRequestDTO dto) {
        return Configuracion.builder()
                .idSucursal(dto.getIdSucursal())
                .nombre(dto.getNombre())
                .parametro(dto.getParametro())
                .valorTexto(dto.getValorTexto())
                .descripcion(dto.getDescripcion())
                .estado(Estado.fromCodigo(dto.getEstado()))
                .inicializar();
    }

    @Mapping(target = "idConfiguracion", source = "idConfiguracion")
    @Mapping(target = "idSucursal", source = "idSucursal")
    @Mapping(target = "nombreSucursal", source = "nombreSucursal")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    ConfiguracionResponseDTO toResponse(Configuracion configuracion);

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
