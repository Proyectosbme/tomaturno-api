package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaxPuestoJpaEntity;
import com.empresa.tomaturno.shared.clases.Auditoria;

@Mapper(componentModel = "cdi")
public interface DetalleColaxPuestoOutputMapper {

    @Mapping(target = "id.idPuesto", source = "idPuesto")
    @Mapping(target = "id.idSucursalPuesto", source = "idSucursalPuesto")
    @Mapping(target = "id.idCola", source = "idCola")
    @Mapping(target = "id.idDetalle", source = "idDetalle")
    @Mapping(target = "id.idSucursalCola", source = "idSucursalCola")
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    DetalleColaxPuestoJpaEntity toJpaEntity(DetalleColaxPuesto domain);

    default DetalleColaxPuesto toDomain(DetalleColaxPuestoJpaEntity entity) {
        Auditoria auditoria = Auditoria.reconstituir(
                entity.getUserCreacion(), entity.getFechaCreacion(), null, null);
        return DetalleColaxPuesto.reconstituir(
                entity.getId().getIdPuesto(),
                entity.getId().getIdSucursalPuesto(),
                entity.getId().getIdCola(),
                entity.getId().getIdDetalle(),
                entity.getId().getIdSucursalCola(),
                auditoria);
    }
}
