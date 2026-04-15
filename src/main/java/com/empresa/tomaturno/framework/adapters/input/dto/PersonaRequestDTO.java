package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class PersonaRequestDTO {

    @NotBlank
    private String dui;

    private String nombres;
    private String apellidos;

    /** Formato DD/MM/YYYY tal como viene del escaneo */
    private String fechaNacimiento;

    private String sexo;
}
