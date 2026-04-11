package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpresaNombreRequestDTO {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombre;
}
