package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CrearTurnoRequestDTO {
    @NotNull
    private Long idSucursal;
    @NotNull
    private Long idCola;
    private Long idDetalle; // nullable: solo si la cola tiene detalles
    private Long idPersona; // nullable: solo si se escaneó el DUI
    private Integer tipoCasoEspecial; // nullable: solo si es caso especial
}
