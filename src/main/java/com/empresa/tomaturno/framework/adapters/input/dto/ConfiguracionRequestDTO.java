package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ConfiguracionRequestDTO {

    @NotNull(message = "La sucursal es obligatoria")
    public Long idSucursal;

    @NotBlank(message = "El nombre de la configuración es obligatorio")
    @Size(max = 100)
    public String nombre;

    /** Valor numérico: 0/1 para flags booleanos, número para correlativos */
    public Integer parametro;

    /** Valor de texto: prefijos, formatos (ej: "C-", "P-") */
    @Size(max = 200)
    public String valorTexto;

    @Size(max = 500)
    public String descripcion;

    @NotNull(message = "El estado es obligatorio")
    public Integer estado;
}
