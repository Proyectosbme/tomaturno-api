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
public class UsuarioRegistroRequestDTO extends AuditRequestDTO {

    @NotNull
    private Long idSucursal;

    private Long idPuesto;

    private Integer correlativo;

    @NotBlank
    @Size(max = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    private String apellidos;

    @Size(max = 20)
    private String dui;

    @Size(max = 20)
    private String telefono;

    @NotBlank
    @Size(max = 20)
    private String perfil;
}
