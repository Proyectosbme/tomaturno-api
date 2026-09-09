package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaxPuestoJpaEntity;

@Mapper(componentModel = "cdi")
public interface DetalleColaxPuestoOutputMapper {

    @Mapping(target = "id.idPuesto", source = "idPuesto")
    @Mapping(target = "id.idSucursalPuesto", source = "idSucursalPuesto")
    @Mapping(target = "id.idCola", source = "idCola")
    @Mapping(target = "id.idDetalle", source = "idDetalle")
    @Mapping(target = "id.idSucursalCola", source = "idSucursalCola")
    @Mapping(target = "prioridad", source = "prioridad")
    @Mapping(target = "userCreacion", source = "auditoria.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoria.fecha")
    DetalleColaxPuestoJpaEntity toJpaEntity(DetalleColaxPuesto domain);

    default DetalleColaxPuesto toDomain(DetalleColaxPuestoJpaEntity entity) {
        Auditoria auditoria = Auditoria.reconstituir(
                entity.getUserCreacion(), entity.getFechaCreacion());
        return DetalleColaxPuesto.of(new DetalleColaxPuesto.Builder()
                .idPuesto(entity.getId().getIdPuesto())
                .idSucursalPuesto(entity.getId().getIdSucursalPuesto())
                .idCola(entity.getId().getIdCola())
                .idDetalle(entity.getId().getIdDetalle())
                .idSucursalCola(entity.getId().getIdSucursalCola())
                .prioridad(entity.getPrioridad())
                .auditoria(auditoria));
    }
}
