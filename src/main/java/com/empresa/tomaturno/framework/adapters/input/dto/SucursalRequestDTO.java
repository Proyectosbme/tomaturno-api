package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SucursalRequestDTO extends AuditRequestDTO {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;

    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "El correo no debe exceder 100 caracteres")
    private String correo;

    @Size(max = 200, message = "La dirección no debe exceder 200 caracteres")
    private String direccion;

    @NotNull(message = "El estado es obligatorio (1=ACTIVO, 0=INACTIVO)")
    private Integer estado;
}
