package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {
    @NotNull(message = "El código de usuario es obligatorio")
    private String codigoUsuario;
    @NotNull(message = "La contraseña es obligatoria")
    private String contrasena;
    @NotNull(message = "La sucursal es obligatoria")
    private Long idSucursal;
}
