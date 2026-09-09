package com.empresa.tomaturno.framework.adapters.input.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.vo.Auditoria;
import com.empresa.tomaturno.configuracion.dominio.vo.Estado;
import com.empresa.tomaturno.framework.adapters.input.dto.ConfiguracionRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ConfiguracionResponseDTO;

@Mapper(componentModel = "cdi")
public interface ConfiguracionInputMapper {

    /** Configuración nueva: la auditoría de creación se arma aquí con el usuario autenticado. */
    default Configuracion toDomain(ConfiguracionRequestDTO dto, String usuario) {
        return Configuracion.of(new Configuracion.Builder()
                .idSucursal(dto.getIdSucursal())
                .nombre(dto.getNombre())
                .parametro(dto.getParametro())
                .descripcion(dto.getDescripcion())
                .estado(Estado.fromCodigo(dto.getEstado()))
                .auditoriaCreacion(Auditoria.of(usuario, LocalDateTime.now())));
    }

    @Mapping(target = "idConfiguracion", source = "idConfiguracion")
    @Mapping(target = "idSucursal", source = "idSucursal")
    @Mapping(target = "nombreSucursal", source = "nombreSucursal")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    ConfiguracionResponseDTO toResponse(Configuracion configuracion);

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
