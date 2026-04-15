package com.empresa.tomaturno.puesto.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.puesto.dominio.exceptions.PuestoValidationException;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.puesto.dominio.vo.Sucursal;

public class Puesto {

    private Long identificador;
    private String nombre;
    private String nombreLlamada;
    private Estado estado;
    private Sucursal sucursal;
    private Auditoria auditoria;

    private Puesto() {
    }

    // ─── Factory methods ──────────────────────────────────────────────────

    public static Puesto inicializar(String nombre, String nombreLlamada, Estado estado, Sucursal sucursal) {
        String nombreNormalizado = nombre != null ? nombre.trim().toUpperCase() : null;
        String nombreLlamadaNormalizado = nombreLlamada != null ? nombreLlamada.trim().toUpperCase() : null;
        Puesto p = new Puesto();
        p.nombre = nombreNormalizado;
        p.nombreLlamada = nombreLlamadaNormalizado;
        p.estado = estado;
        p.sucursal = sucursal;
        return p;
    }

    public static Puesto reconstituir(Long identificador, String nombre, String nombreLlamada,
            Estado estado, Sucursal sucursal, Auditoria auditoria) {
        Puesto p = new Puesto();
        p.identificador = identificador;
        p.nombre = nombre;
        p.nombreLlamada = nombreLlamada;
        p.estado = estado;
        p.sucursal = sucursal;
        p.auditoria = auditoria;
        return p;
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    public void crear(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        validarCreacion();
    }

    public void modificar(String nombre, String nombreLlamada, Estado estado, String usuario) {
        if(nombre != null) {
            this.nombre = nombre.trim().toUpperCase();
        }
        if(nombreLlamada != null) {
            this.nombreLlamada = nombreLlamada.trim().toUpperCase();
        }   
        if(estado != null) {    
                    this.estado = estado;
        }
        if(usuario != null) {
            this.auditoria = this.auditoria.conModificacion(usuario, LocalDateTime.now());
        }
        validarModificacion();
    }

    public void validarNombreUnico(boolean existeNombreEnSucursal) {
        if (existeNombreEnSucursal) {
            throw new PuestoValidationException(
                    "Ya existe un puesto con el nombre '" + this.nombre + "' en esta sucursal");
        }
    }

    private void validarCreacion() {
        if (this.nombre == null || this.nombre.isEmpty()) {
            throw new PuestoValidationException("El nombre del puesto es obligatorio");
        }
        if (this.estado == null) {
            throw new PuestoValidationException("El estado del puesto es obligatorio");
        }
        if (this.sucursal == null) {
            throw new PuestoValidationException("La sucursal del puesto es obligatoria");
        }
    }

    private void validarModificacion() {
        if (this.identificador == null) {
            throw new PuestoValidationException("El identificador del puesto es obligatorio");
        }
        validarCreacion();
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreLlamada() {
        return nombreLlamada;
    }

    public Estado getEstado() {
        return estado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }
}
