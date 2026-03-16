package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleColaxPuestoRequestDTO {

    @NotNull
    private Long idPuesto;

    @NotNull
    private Long idSucursalPuesto;

    @NotNull
    private Long idCola;

    @NotNull
    private Long idDetalle;

    @NotNull
    private Long idSucursalCola;
}
