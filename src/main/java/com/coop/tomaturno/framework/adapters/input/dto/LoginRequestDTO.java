package com.coop.tomaturno.framework.adapters.input.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {
    private String codigoUsuario;
    private String contrasena;
    private Long idSucursal;
}
