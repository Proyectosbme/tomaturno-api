package com.coop.tomaturno.framework.adapters.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PersonaResponseDTO {
    private Long id;
    private String dui;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;
}
