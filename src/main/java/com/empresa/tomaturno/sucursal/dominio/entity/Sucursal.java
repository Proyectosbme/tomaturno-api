package com.empresa.tomaturno.sucursal.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;
import com.empresa.tomaturno.sucursal.dominio.vo.Estado;

public class Sucursal {

    private Long identificador;
    private String nombre;
    private Contacto contacto;
    private Estado estado;
    private Auditoria auditoria;

    private Sucursal(Long identificador, String nombre, Contacto contacto, Estado estado, Auditoria auditoria) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.contacto = contacto;
        this.estado = estado;
        this.auditoria = auditoria;
    }

    public static Sucursal crear(String nombre, Contacto contacto, Estado estado) {
        if (nombre == null || nombre.isBlank()) {
            throw new SucursalValidationException("El nombre de la sucursal no puede ser nulo o vacío");
        }
        if (contacto == null) {
            throw new SucursalValidationException("El contacto de la sucursal no puede ser nulo");
        }
        if (estado == null) {
            throw new SucursalValidationException("El estado de la sucursal no puede ser nulo");
        }
        return new Sucursal(null, nombre, contacto, estado, null);
    }

    public static Sucursal reconstituir(Long id, String nombre, Contacto contacto, Estado estado, Auditoria auditoria) {
        return new Sucursal(id, nombre, contacto, estado, auditoria);
    }

    public static Sucursal referencia(Long id, String nombre) {
        return new Sucursal(id, nombre, null, null, null);
    }

    public void auditoriaCreacion(String usuario, LocalDateTime fecha) {
        this.auditoria = Auditoria.deCreacion(usuario, fecha);
    }

    public void auditoriaModificacion(String usuario, LocalDateTime fecha) {
        this.auditoria = this.auditoria.conModificacion(usuario, fecha);
    }

    public void modificar(String nombre, Contacto contacto, Estado estado) {
        if (nombre == null || nombre.isBlank()) {
            throw new SucursalValidationException("El nombre de la sucursal no puede ser nulo o vacío");
        }
        if (contacto == null) {
            throw new SucursalValidationException("El contacto de la sucursal no puede ser nulo");
        }
        if (estado == null) {
            throw new SucursalValidationException("El estado de la sucursal no puede ser nulo");
        }
        this.nombre = nombre;
        this.contacto = contacto;
        this.estado = estado;
    }

    public Long getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public Estado getEstado() {
        return estado;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }
}
