package com.coop.tomaturno.puesto.dominio.entity;

import java.time.LocalDateTime;

import com.coop.tomaturno.puesto.dominio.exceptions.PuestoValidationException;
import com.coop.tomaturno.puesto.dominio.vo.Auditoria;
import com.coop.tomaturno.puesto.dominio.vo.Estado;
import com.coop.tomaturno.puesto.dominio.vo.Sucursal;

public class Puesto {

    private Long identificador;
    private String nombre;
    private String nombreLlamada;
    private Estado estado;
    private Sucursal sucursal;
    private Auditoria auditoria;

    public Puesto() {
    }

    public Puesto(String nombre, String nombreLlamada, Estado estado, Sucursal sucursal, Auditoria auditoria) {
        this.nombre = nombre;
        this.nombreLlamada = nombreLlamada;
        this.estado = estado;
        this.sucursal = sucursal;
        this.auditoria = auditoria;
    }

    public Puesto(Long identificador, String nombre, String nombreLlamada, Estado estado, Sucursal sucursal, Auditoria auditoria) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.nombreLlamada = nombreLlamada;
        this.estado = estado;
        this.sucursal = sucursal;
        this.auditoria = auditoria;
    }

    public void modificar(String nombre, String nombreLlamada, Estado estado) {
        this.nombre = nombre;
        this.nombreLlamada = nombreLlamada;
        this.estado = estado;
    }

    public void validarNombreUnico(boolean existeNombreEnSucursal) {
        if (existeNombreEnSucursal) {
            throw new PuestoValidationException(
                    "Ya existe un puesto con el nombre '" + this.nombre + "' en esta sucursal");
        }
    }

    public void validarCreacion() {
        if (this.nombre == null || this.nombre.isEmpty()) {
            throw new PuestoValidationException("El nombre del puesto es obligatorio");
        }
        if (this.estado == null) {
            throw new PuestoValidationException("El estado del puesto es obligatorio");
        }
        if (this.sucursal == null) {
            throw new PuestoValidationException("La sucursal del puesto es obligatoria");
        }
        auditoria.validarCreacion();
    }

    public void validarModificacion() {
        if (this.identificador == null) {
            throw new PuestoValidationException("El identificador del puesto es obligatorio");
        }
        this.validarCreacion();
        auditoria.validarModificacion();
    }

    public void crearSucursal(Long identificador, String nombre) {
        this.sucursal = new Sucursal(identificador, nombre);
    }

    public void auditoriaCreacion(String usuarioCreacion, LocalDateTime fechaCreacion) {
        if (this.auditoria == null) {
            this.auditoria = new Auditoria();
        }
        this.auditoria.creacion(usuarioCreacion, fechaCreacion);
    }

    public void auditoriaModificacion(String usuarioModificacion, LocalDateTime fechaModificacion) {
        if (this.auditoria == null) {
            throw new PuestoValidationException("No tiene usuario de creacion y modificacion revisar ");
        }
        this.auditoria.modificacion(usuarioModificacion, fechaModificacion);
    }

    public Long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public String getNombreLlamada() {
        return nombreLlamada;
    }

    public void setNombreLlamada(String nombreLlamada) {
        this.nombreLlamada = nombreLlamada;
    }

    
}
