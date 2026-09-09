package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;

import com.empresa.tomaturno.framework.adapters.input.dto.TiempoMuertoResponseDTO;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.VwUsuarioEstadoOperadorJpaEntity;

/**
 * Mapea directo de la entidad JPA (vista vwusuarioestadooperador) al DTO de respuesta,
 * sin pasar por una capa de dominio — es un reporte de solo lectura, no un agregado
 * de negocio.
 */
@Mapper(componentModel = "cdi")
public interface TiempoMuertoInputMapper {
    TiempoMuertoResponseDTO toResponse(VwUsuarioEstadoOperadorJpaEntity entity);
}
