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

    default Persona toDomain(PersonaJpaEntity e) {
        return Persona.builder()
                .id(e.getId())
                .dui(e.getDui())
                .nombres(e.getNombres())
                .apellidos(e.getApellidos())
                .fechaNacimiento(e.getFechaNacimiento())
                .sexo(e.getSexo())
                .foto(e.getFoto())
                .fechaCreacion(e.getFechaCreacion())
                .fechaModificacion(e.getFechaModificacion())
                .build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dui", ignore = true)
    void updateEntityFromDomain(Persona persona, @MappingTarget PersonaJpaEntity entity);
}
