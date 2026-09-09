package com.empresa.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;

public class EstadoOperadorResponseDTO {

    private Long id;
    private Long idUsuario;
    private Long idSucursal;
    private Long idPuesto;
    private Long idEstadoOperador;
    private Long idTipoDescanso;
    private String comentario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String userCreacion;
    private LocalDateTime fechaCreacion;
    private String userModificacion;
    private LocalDateTime fechaModificacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Long idSucursal) { this.idSucursal = idSucursal; }
    public Long getIdPuesto() { return idPuesto; }
    public void setIdPuesto(Long idPuesto) { this.idPuesto = idPuesto; }
    public Long getIdEstadoOperador() { return idEstadoOperador; }
    public void setIdEstadoOperador(Long idEstadoOperador) { this.idEstadoOperador = idEstadoOperador; }
    public Long getIdTipoDescanso() { return idTipoDescanso; }
    public void setIdTipoDescanso(Long idTipoDescanso) { this.idTipoDescanso = idTipoDescanso; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public String getUserCreacion() { return userCreacion; }
    public void setUserCreacion(String userCreacion) { this.userCreacion = userCreacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getUserModificacion() { return userModificacion; }
    public void setUserModificacion(String userModificacion) { this.userModificacion = userModificacion; }
    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}
