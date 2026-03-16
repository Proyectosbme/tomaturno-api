package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PersonaJpaEntity;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

@Mapper(componentModel = "cdi")
public interface PersonaMapper {

    @Mapping(target = "id", ignore = true)
    PersonaJpaEntity toEntity(Persona persona);

    Persona toDomain(PersonaJpaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dui", ignore = true)
    void updateEntityFromDomain(Persona persona, @MappingTarget PersonaJpaEntity entity);
}
