package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfiguracionRequestDTO extends AuditRequestDTO {

    @NotNull(message = "La sucursal es obligatoria")
    public Long idSucursal;

    @Size(max = 100)
    public String nombre;

    @NotNull(message = "El parametro es obligatorio")
    public Integer parametro;

    @Size(max = 500)
    public String descripcion;

    @NotNull(message = "El estado es obligatorio")
    public Integer estado;
}
