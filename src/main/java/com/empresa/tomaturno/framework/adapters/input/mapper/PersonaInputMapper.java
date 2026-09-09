package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;

import com.empresa.tomaturno.framework.adapters.input.dto.PersonaRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.PersonaResponseDTO;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "cdi")
public interface PersonaInputMapper {

    /**
     * Solicitud entrante: se arma con fechaCreacion = ahora. Si el caso de uso determina que
     * ya existe una persona con ese DUI, esa misma marca de tiempo se reutiliza como
     * fechaModificacion en vez de descartarse.
     */
    default Persona toDomain(PersonaRequestDTO dto) {
        return Persona.of(new Persona.Builder()
                .dui(dto.getDui())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(parseFecha(dto.getFechaNacimiento()))
                .sexo(dto.getSexo())
                .fechaCreacion(LocalDateTime.now()));
    }

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
