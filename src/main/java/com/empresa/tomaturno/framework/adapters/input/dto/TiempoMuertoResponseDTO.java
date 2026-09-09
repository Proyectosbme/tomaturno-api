package com.empresa.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TiempoMuertoResponseDTO {
    private Long id;
    private Long idUsuario;
    private Long idSucursal;
    private Long idPuesto;
    private String codigoUsuario;
    private String nombreCompleto;
    private String puesto;
    private Long idEstadoOperador;
    private String estadoOperador;
    private Long idTipoDescanso;
    private String tipoDescanso;
    private String comentario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
