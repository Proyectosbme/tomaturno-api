package com.empresa.tomaturno.sucursal.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;
import com.empresa.tomaturno.shared.clases.*;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;

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

    public static Sucursal inicializar(String nombre, Contacto contacto, Estado estado) {

        return new Sucursal(null, nombre, contacto, estado, null);
    }

    public static Sucursal reconstituir(Long id, String nombre, Contacto contacto, Estado estado, Auditoria auditoria) {
        return new Sucursal(id, nombre, contacto, estado, auditoria);
    }

    public static Sucursal referencia(Long id, String nombre) {
        return new Sucursal(id, nombre, null, null, null);
    }

    public void crear(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        this.validarCreacion();

    }

    private void validarCreacion() {
        if (nombre == null || nombre.isBlank()) {
            throw new SucursalValidationException("El nombre de la sucursal no puede ser nulo o vacío");
        }
        if (contacto == null) {
            throw new SucursalValidationException("El contacto de la sucursal no puede ser nulo");
        }
        if (estado == null) {
            throw new SucursalValidationException("El estado de la sucursal no puede ser nulo");
        }
        if (this.auditoria == null || this.auditoria.getUsuarioCreacion() == null
                || this.auditoria.getFechaCreacion() == null) {
            throw new SucursalValidationException("La sucursal debe tener auditoria de creación");
        }
    }

    private void auditoriaCreacion(String usuario, LocalDateTime fecha) {

    }

    public void modificar(String nombre, Contacto contacto, Estado estado, String usuario) {
        this.asignadatosModificacion(nombre, contacto, estado, usuario);
        this.validarModificacion();

    }

    private void asignadatosModificacion(String nombre, Contacto contacto, Estado estado, String usuario) {
        this.nombre = nombre;
        this.contacto = contacto;
        this.estado = estado;
        this.auditoria = this.auditoria.conModificacion(usuario, LocalDateTime.now());
    }

    private void validarModificacion() {
        if (this.identificador == null) {
            throw new SucursalValidationException("La sucursal debe tener un identificador para ser modificada");
        }
        if (this.nombre == null || nombre.isBlank()) {
            throw new SucursalValidationException("El nombre de la sucursal no puede ser nulo o vacío");
        }

        if (this.contacto == null) {
            throw new SucursalValidationException("El contacto de la sucursal no puede ser nulo");
        }
        if (this.estado == null) {
            throw new SucursalValidationException("El estado de la sucursal no puede ser nulo");
        }
        if (this.auditoria == null || this.auditoria.getUsuarioCreacion() == null
                || this.auditoria.getFechaCreacion() == null) {
            throw new SucursalValidationException("La sucursal debe tener auditoria de creación para ser modificada");
        }
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
