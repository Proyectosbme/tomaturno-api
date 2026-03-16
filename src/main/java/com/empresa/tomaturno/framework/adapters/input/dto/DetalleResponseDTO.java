package com.coop.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleResponseDTO {
    private Long idDetalle;
    private Long idCola;
    private Long idSucursal;
    private String nombre;
    private String codigo;
    private Integer estado;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;
}