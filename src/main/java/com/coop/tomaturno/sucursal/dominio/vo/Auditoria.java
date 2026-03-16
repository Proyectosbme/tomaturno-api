package com.coop.tomaturno.sucursal.dominio.vo;

import java.time.LocalDateTime;

import com.coop.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;

public class Auditoria {

    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;

    public void creacion(String usuarioCreacion, LocalDateTime fechaCreacion) {
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public void modificacion(String usuarioModificacion, LocalDateTime fechaModificacion) {
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
    }

    public void validarCreacion() {
        if (this.usuarioCreacion == null || this.fechaCreacion == null) {
            throw new SucursalValidationException("No tiene usuario de creacion y fecha de creacion revisar ");
        }
    }

    public void validarModificacion() {
        this.validarCreacion();
        if (this.usuarioModificacion == null || this.fechaModificacion == null) {
            throw new SucursalValidationException("No tiene usuario de modificacion y fecha de modificacion revisar ");
        }
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }


    @Override
    public String toString() {
        return "Auditoria [usuarioCreacion=" + usuarioCreacion + ", fechaCreacion=" + fechaCreacion
                + ", usuarioModificacion=" + usuarioModificacion + ", fechaModificacion=" + fechaModificacion + "]";
    }

    

}
