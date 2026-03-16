package com.coop.tomaturno.framework.adapters.input.dto;

import java.time.LocalDateTime;

public class ConfiguracionResponseDTO {

    private Long idConfiguracion;
    private Long idSucursal;
    private String nombreSucursal;
    private String nombre;
    private Integer parametro;
    private String valorTexto;
    private String descripcion;
    private Integer estado;
    private String userCreacion;
    private LocalDateTime fechaCreacion;
    private String userModificacion;
    private LocalDateTime fechaModificacion;

    public Long getIdConfiguracion() { return idConfiguracion; }
    public void setIdConfiguracion(Long idConfiguracion) { this.idConfiguracion = idConfiguracion; }
    public Long getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Long idSucursal) { this.idSucursal = idSucursal; }
    public String getNombreSucursal() { return nombreSucursal; }
    public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getParametro() { return parametro; }
    public void setParametro(Integer parametro) { this.parametro = parametro; }
    public String getValorTexto() { return valorTexto; }
    public void setValorTexto(String valorTexto) { this.valorTexto = valorTexto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }
    public String getUserCreacion() { return userCreacion; }
    public void setUserCreacion(String userCreacion) { this.userCreacion = userCreacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getUserModificacion() { return userModificacion; }
    public void setUserModificacion(String userModificacion) { this.userModificacion = userModificacion; }
    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}
