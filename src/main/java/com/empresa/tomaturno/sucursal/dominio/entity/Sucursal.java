package com.empresa.tomaturno.sucursal.dominio.entity;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;
import com.empresa.tomaturno.sucursal.dominio.validador.ValidadorNulosVacios;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;

public final class Sucursal {

    private final Long identificador;
    private String nombre;
    private Contacto contacto;
    private Estado estado;
    private Auditoria auditoriaCreacion;
    private Auditoria auditoriaModificacion;

    private Sucursal(Builder builder) {
        this.identificador = builder.identificador;
        this.nombre = builder.nombre != null ? builder.nombre.trim().toUpperCase() : null;
        this.contacto = builder.contacto;
        this.estado = builder.estado;
        this.auditoriaCreacion = builder.auditoriaCreacion;
        this.auditoriaModificacion = builder.auditoriaModificacion;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /**
     * Único punto de creación/reconstitución: valida el builder antes de construir.
     */
    public static Sucursal of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    /**
     * auditoriaModificacion ya viene construida (Auditoria.of(usuario, fecha));
     * esta entidad no la arma.
     */
    public void modificar(String nombre, Contacto contacto, Estado estado, Auditoria auditoriaModificacion) {
        if (nombre != null) {
            this.nombre = nombre.trim().toUpperCase();
        }
        if (contacto != null) {
            this.contacto = contacto;
        }
        if (estado != null) {
            this.estado = estado;
        }
        aplicarAuditoriaModificacion(auditoriaModificacion);
    }

    private void aplicarAuditoriaModificacion(Auditoria auditoriaModificacion) {
        ValidadorNulosVacios
                .variable(auditoriaModificacion, "La auditoria de modificacion de la sucursal",
                        SucursalValidationException::new)
                .noNulo();
        this.auditoriaModificacion = auditoriaModificacion;
    }

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado
     * inválido.
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.nombre, "El nombre de la sucursal", SucursalValidationException::new)
                .noNuloNoVacio();
        ValidadorNulosVacios.variable(builder.contacto, "El contacto de la sucursal", SucursalValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.estado, "El estado de la sucursal", SucursalValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.auditoriaCreacion, "La auditoria de creacion de la sucursal",
                        SucursalValidationException::new)
                .noNulo();
    }

    // ─── Getters ──────────────────────────────────────────────────────────

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

    public Auditoria getAuditoriaCreacion() {
        return auditoriaCreacion;
    }

    public Auditoria getAuditoriaModificacion() {
        return auditoriaModificacion;
    }

    public static class Builder {
        private Long identificador;
        private String nombre;
        private Contacto contacto;
        private Estado estado;
        private Auditoria auditoriaCreacion;
        private Auditoria auditoriaModificacion;

        public Builder identificador(Long identificador) {
            this.identificador = identificador;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder contacto(Contacto contacto) {
            this.contacto = contacto;
            return this;
        }

        public Builder estado(Estado estado) {
            this.estado = estado;
            return this;
        }

        public Builder auditoriaCreacion(Auditoria auditoriaCreacion) {
            this.auditoriaCreacion = auditoriaCreacion;
            return this;
        }

        public Builder auditoriaModificacion(Auditoria auditoriaModificacion) {
            this.auditoriaModificacion = auditoriaModificacion;
            return this;
        }

        private Sucursal build() {
            return new Sucursal(this);
        }
    }
}
