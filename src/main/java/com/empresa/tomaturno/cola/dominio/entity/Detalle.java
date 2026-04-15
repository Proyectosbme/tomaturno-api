package com.empresa.tomaturno.cola.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

public class Detalle {

    private Long correlativo;
    private String nombre;
    private String codigo;
    private Estado estado;
    private Auditoria auditoria;

    private Detalle() {
    }

    public static Detalle inicializar(String nombre, String codigo, Estado estado) {
        Detalle d = new Detalle();
        d.nombre = nombre != null ? nombre.trim().toUpperCase() : null;
        d.codigo = codigo != null ? codigo.trim().toUpperCase() : null;
        d.estado = estado;
        return d;
    }

    public static Detalle reconstituir(Long correlativo, String nombre, String codigo,
            Estado estado, Auditoria auditoria) {
        Detalle d = new Detalle();
        d.correlativo = correlativo;
        d.nombre = nombre;
        d.codigo = codigo;
        d.estado = estado;
        d.auditoria = auditoria;
        return d;
    }

    public void crear(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        validarCreacion();
    }

    public void modificar(String nombre, String codigo, Estado estado, String usuario) {
        if(nombre != null) {
            this.nombre = nombre.trim().toUpperCase();
        }
        if(codigo != null) {
            this.codigo = codigo.trim().toUpperCase();
        }   
        if(estado != null) {
            this.estado = estado;
        }
        if(this.auditoria != null) {
            this.auditoria = this.auditoria.conModificacion(usuario, LocalDateTime.now());
        } 
        validarModificacion();
    }

    private void validarCreacion() {
        if (this.nombre == null || this.nombre.isEmpty()) {
            throw new ColaValidationException("El nombre del detalle es obligatorio");
        }
        if (this.codigo == null || this.codigo.isEmpty()) {
            throw new ColaValidationException("El codigo del detalle es obligatorio");
        }
        if (this.estado == null) {
            throw new ColaValidationException("El estado del detalle es obligatorio");
        }
    }

    private void validarModificacion() {
        if (this.correlativo == null) {
            throw new ColaValidationException("El correlativo del detalle es obligatorio");
        }
        validarCreacion();
    }

    public Long getCorrelativo() {
        return correlativo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }
}
