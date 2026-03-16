package com.empresa.tomaturno.cola.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;

public class Detalle {

    Long correlativo;
    String nombre;
    String codigo;
    Estado estado;
    Auditoria auditoria;

    public Detalle() {
    }   

    public Detalle(Long correlativo, String nombre, String codigo, Estado estado, Auditoria auditoria) {
        this.correlativo = correlativo;
        this.nombre = nombre;
        this.codigo = codigo;
        this.estado = estado;
        this.auditoria = auditoria;
    }

    
    public Detalle(String nombre, String codigo, Estado estado, Auditoria auditoria) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.estado = estado;
        this.auditoria = auditoria;
    }

    public void modificar(String nombre, String codigo, Estado estado) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.estado = estado;
    }

   
    public void auditoriaCreacion(String usuarioCreacion, LocalDateTime fechaCreacion) {
        if (this.auditoria == null) {
            this.auditoria = new Auditoria();
        }
        this.auditoria.creacion(usuarioCreacion, fechaCreacion);
    }

    public void auditoriaModificacion(String usuarioModificacion, LocalDateTime fechaModificacion) {
        if (this.auditoria == null) {
            throw new ColaValidationException("No tiene usuario de creacion y modificacion revisar ");
        }
        this.auditoria.modificacion(usuarioModificacion, fechaModificacion);
    }

    public void validarCreacion() {
        if (this.nombre == null || this.nombre.isEmpty()) {
            throw new ColaValidationException("El nombre del detalle es obligatorio");
        }
        if (this.codigo == null || this.codigo.isEmpty()) {
            throw new ColaValidationException("El codigo del detalle es obligatorio");
        }
        if (this.estado == null) {
            throw new ColaValidationException("El estado del detalle es obligatorio");
        }
        auditoria.validarCreacion();
    }

    public void validarModificacion() {
        if (this.correlativo == null) {
            throw new ColaValidationException("El correlativo del detalle es obligatorio");
        }
        this.validarCreacion();
        auditoria.validarModificacion();
    }   

    public Long getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(Long correlativo) {
        this.correlativo = correlativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    
    
}
