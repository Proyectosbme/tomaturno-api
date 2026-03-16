package com.coop.tomaturno.framework.adapters.output.mapper;

import com.coop.tomaturno.framework.adapters.output.persistencia.entity.PersonaJpaEntity;
import com.coop.tomaturno.persona.dominio.entity.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface PersonaMapper {

    @Mapping(target = "id", ignore = true)
    PersonaJpaEntity toEntity(Persona persona);

    Persona toDomain(PersonaJpaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dui", ignore = true)
    void updateEntityFromDomain(Persona persona, @MappingTarget PersonaJpaEntity entity);
}
