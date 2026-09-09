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
public class DescansoRequestDTO {

    @NotNull(message = "El idTipoDescanso es obligatorio")
    private Long idTipoDescanso;

    /** Obligatorio cuando idTipoDescanso es OTRO — se valida en el dominio (EstadoOperador). */
    private String comentario;
}
