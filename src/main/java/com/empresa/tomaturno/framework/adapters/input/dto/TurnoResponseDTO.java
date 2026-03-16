package com.empresa.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TurnoResponseDTO {
    private Long id;
    private Long idSucursal;
    private LocalDateTime fechaCreacion;
    private String codigoTurno;
    private LocalDateTime fechaLlamada;
    private LocalDateTime fechaFinalizacion;
    private Long idCola;
    private Long idDetalle;
    private Integer estado;
    private String descripcionEstado;
    private Long idTurnoRelacionado;
    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idUsuario;
    private Long idPersona;
    private Integer tipoCasoEspecial;
    private String nombreLlamada;
}
