package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @NotNull
    @Min(1)
    @Max(50)
    private Integer prioridad;

}
