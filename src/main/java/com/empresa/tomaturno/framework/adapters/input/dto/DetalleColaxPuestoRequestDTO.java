package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetalleColaxPuestoRequestDTO extends AuditRequestDTO {

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
