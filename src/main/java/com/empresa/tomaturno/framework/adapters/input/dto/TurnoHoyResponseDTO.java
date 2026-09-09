package com.empresa.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TurnoHoyResponseDTO {
    private Long id;
    private String codigoTurno;
    private Long idSucursalTicket;
    private String sucursalTicket;
    private Long idUsuario;
    private String nombreCompleto;
    private String codigoUsuario;
    private Long idPuesto;
    private Long idPuestoSucursal;
    private String puesto;
    private Long idCola;
    private String cola;
    private Long idDetalle;
    private String detalle;
    private Integer tipoCasoEspecial;
    private String casoEspecial;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLlamada;
    private LocalDateTime fechaFinalizacion;
    private Long idCatalogoEstado;
    private Long idCatalogoEstadoDetalle;
    private String estadoTurno;
    private Long idTurnoRelacionado;
}
