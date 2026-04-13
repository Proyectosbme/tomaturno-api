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
public class LlamarSiguienteTurnoRequestDTO {

    @NotNull
    private Long idSucursal;

    @NotNull
    private Long idPuesto;

    @NotNull
    private Long idSucursalPuesto;

    private Long idUsuario;
}
