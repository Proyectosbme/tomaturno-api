package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotNull
    private Long idSucursal;

    private Long idPuesto;

    private Integer correlativo;

    private Integer atenderCasosEspeciales;

    @NotBlank
    @Size(min = 2, max = 50)
    private String codigoUsuario;

    @Size(max = 100)
    private String contrasena;

    @NotBlank
    @Size(max = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    private String apellidos;

    @Size(max = 20)
    private String dui;

    @NotNull
    private Integer estado;

    @Size(max = 20)
    private String telefono;

    @Size(max = 50)
    private String ip;

    @NotBlank
    @Size(max = 20)
    private String perfil;

    @Size(max = 20)
    private String perfilCreador;
}
