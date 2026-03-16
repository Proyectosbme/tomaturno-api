package com.empresa.tomaturno.detallecolaxpuesto.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.exceptions.DetalleColaxPuestoValidationException;

public class DetalleColaxPuesto {

    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idCola;
    private Long idDetalle;
    private Long idSucursalCola;
    private String userCreacion;
    private LocalDateTime fechaCreacion;
    // Campos enriquecidos (no persistidos)
    private String nombreCola;
    private String nombreDetalle;

    public DetalleColaxPuesto() {
    }

    public DetalleColaxPuesto(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola) {
        this.idPuesto = idPuesto;
        this.idSucursalPuesto = idSucursalPuesto;
        this.idCola = idCola;
        this.idDetalle = idDetalle;
        this.idSucursalCola = idSucursalCola;
    }

    public void auditoriaCreacion(String usuario, LocalDateTime fecha) {
        this.userCreacion = usuario;
        this.fechaCreacion = fecha;
    }

    public void validarAsignacion() {
        if (this.idPuesto == null) {
            throw new DetalleColaxPuestoValidationException("El id del puesto es obligatorio");
        }
        if (this.idSucursalPuesto == null) {
            throw new DetalleColaxPuestoValidationException("El id de la sucursal del puesto es obligatorio");
        }
        if (this.idCola == null) {
            throw new DetalleColaxPuestoValidationException("El id de la cola es obligatorio");
        }
        if (this.idDetalle == null) {
            throw new DetalleColaxPuestoValidationException("El id del detalle es obligatorio");
        }
        if (this.idSucursalCola == null) {
            throw new DetalleColaxPuestoValidationException("El id de la sucursal de la cola es obligatorio");
        }
    }

    public Long getIdPuesto() { return idPuesto; }
    public void setIdPuesto(Long idPuesto) { this.idPuesto = idPuesto; }

    public Long getIdSucursalPuesto() { return idSucursalPuesto; }
    public void setIdSucursalPuesto(Long idSucursalPuesto) { this.idSucursalPuesto = idSucursalPuesto; }

    public Long getIdCola() { return idCola; }
    public void setIdCola(Long idCola) { this.idCola = idCola; }

    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }

    public Long getIdSucursalCola() { return idSucursalCola; }
    public void setIdSucursalCola(Long idSucursalCola) { this.idSucursalCola = idSucursalCola; }

    public String getUserCreacion() { return userCreacion; }
    public void setUserCreacion(String userCreacion) { this.userCreacion = userCreacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getNombreCola() { return nombreCola; }
    public void setNombreCola(String nombreCola) { this.nombreCola = nombreCola; }

    public String getNombreDetalle() { return nombreDetalle; }
    public void setNombreDetalle(String nombreDetalle) { this.nombreDetalle = nombreDetalle; }
}
