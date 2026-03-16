package com.coop.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;
import com.coop.tomaturno.configuracion.dominio.vo.Estado;
import com.coop.tomaturno.framework.adapters.input.dto.ConfiguracionRequestDTO;
import com.coop.tomaturno.framework.adapters.input.dto.ConfiguracionResponseDTO;

@Mapper(componentModel = "cdi")
public interface ConfiguracionInputMapper {

    @Mapping(target = "idConfiguracion", ignore = true)
    @Mapping(target = "auditoria", ignore = true)
    @Mapping(target = "nombreSucursal", ignore = true)
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    Configuracion toDomain(ConfiguracionRequestDTO dto);

    @Mapping(target = "idConfiguracion", source = "idConfiguracion")
    @Mapping(target = "idSucursal", source = "idSucursal")
    @Mapping(target = "nombreSucursal", source = "nombreSucursal")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    ConfiguracionResponseDTO toResponse(Configuracion configuracion);

    @Named("integerToEstado")
    static Estado integerToEstado(Integer codigo) {
        return codigo == null ? null : Estado.fromCodigo(codigo);
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
