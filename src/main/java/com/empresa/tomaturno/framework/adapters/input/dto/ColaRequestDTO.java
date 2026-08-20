package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ColaRequestDTO extends AuditRequestDTO {

    @NotNull(message = "El id de la sucursal es obligatorio")
    private Long idSucursal;

    @NotBlank(message = "El nombre de la cola es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El código de la cola es obligatorio")
    @Size(min = 1, max = 20, message = "El código debe tener entre 1 y 20 caracteres")
    private String codigo;

    @NotNull(message = "El estado es obligatorio (1=ACTIVO, 0=INACTIVO)")
    private Integer estado;

}
