package com.coop.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColaResponseDTO {
    private Long id;
    private Long idSucursal;
    private String nombre;
    private String codigo;
    private Long prioridad;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;
    private Integer estado;
    private String nombreSucursal;
    private List<DetalleResponseDTO> detalles;
}
