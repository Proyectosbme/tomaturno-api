package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.framework.adapters.input.dto.PersonaRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.PersonaResponseDTO;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "cdi")
public interface PersonaInputMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "foto", ignore = true)
    @Mapping(target = "fechaNacimiento", expression = "java(parseFecha(dto.getFechaNacimiento()))")
    Persona toDomain(PersonaRequestDTO dto);

    PersonaResponseDTO toResponse(Persona persona);

    default LocalDate parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isBlank()) return null;
        try {
            return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }
}
