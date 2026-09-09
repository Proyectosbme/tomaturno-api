package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.EmpresaJpaEntity;

@Mapper(componentModel = "cdi")
public interface EmpresaOutputMapper {

    EmpresaJpaEntity toJpaEntity(Empresa empresa);

    default Empresa toDomain(EmpresaJpaEntity e) {
        return Empresa.of(new Empresa.Builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .banner(e.getBanner())
                .logo(e.getLogo()));
    }

    void updateEntityFromDomain(Empresa empresa, @MappingTarget EmpresaJpaEntity entity);
}
