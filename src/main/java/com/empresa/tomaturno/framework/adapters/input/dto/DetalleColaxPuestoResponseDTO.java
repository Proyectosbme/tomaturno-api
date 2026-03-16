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
public class DetalleColaxPuestoResponseDTO {

    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idCola;
    private Long idDetalle;
    private Long idSucursalCola;
    private String nombreCola;
    private String nombreDetalle;
    private String userCreacion;
    private LocalDateTime fechaCreacion;
}
