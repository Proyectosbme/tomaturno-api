package com.coop.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaxPuestoJpaEntity;

@Mapper(componentModel = "cdi")
public interface DetalleColaxPuestoOutputMapper {

    @Mapping(target = "id.idPuesto", source = "idPuesto")
    @Mapping(target = "id.idSucursalPuesto", source = "idSucursalPuesto")
    @Mapping(target = "id.idCola", source = "idCola")
    @Mapping(target = "id.idDetalle", source = "idDetalle")
    @Mapping(target = "id.idSucursalCola", source = "idSucursalCola")
    DetalleColaxPuestoJpaEntity toJpaEntity(DetalleColaxPuesto domain);

    @Mapping(target = "idPuesto", source = "id.idPuesto")
    @Mapping(target = "idSucursalPuesto", source = "id.idSucursalPuesto")
    @Mapping(target = "idCola", source = "id.idCola")
    @Mapping(target = "idDetalle", source = "id.idDetalle")
    @Mapping(target = "idSucursalCola", source = "id.idSucursalCola")
    DetalleColaxPuesto toDomain(DetalleColaxPuestoJpaEntity entity);
}
