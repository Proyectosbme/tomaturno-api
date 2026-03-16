package com.coop.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReasignarTurnoRequestDTO {
    @NotNull
    private Long idSucursalDestino;
    @NotNull
    private Long idColaDestino;
    private Long idDetalleDestino; // nullable
}
