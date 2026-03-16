package com.coop.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRequestDTO {

    @NotBlank(message = "El nombre del detalle es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El código del detalle es obligatorio")
    @Size(min = 1, max = 20, message = "El código debe tener entre 1 y 20 caracteres")
    private String codigo;

    @NotNull(message = "El estado es obligatorio (1=ACTIVO, 0=INACTIVO)")
    private Integer estado;
}