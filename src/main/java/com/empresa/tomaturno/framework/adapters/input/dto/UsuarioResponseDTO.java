package com.coop.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private Long idSucursal;
    private Integer correlativo;
    private Integer atenderCasosEspeciales;
    private String nombreSucursal;
    private Long idPuesto;
    private String nombrePuesto;
    private String codigoUsuario;
    private String nombres;
    private String apellidos;
    private String dui;
    private Integer estado;
    private String telefono;
    private String ip;
    private String perfil;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;
}
