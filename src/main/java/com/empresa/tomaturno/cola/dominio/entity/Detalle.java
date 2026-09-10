package com.empresa.tomaturno.cola.dominio.entity;

import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.cola.dominio.validador.ValidadorNulosVacios;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;

public final class Detalle {

    private final Long correlativo;
    private String nombre;
    private String codigo;
    private Estado estado;
    private Auditoria auditoriaCreacion;
    private Auditoria auditoriaModificacion;

    private Detalle(Builder builder) {
        this.correlativo = builder.correlativo;
        this.nombre = builder.nombre != null ? builder.nombre.trim().toUpperCase() : null;
        this.codigo = builder.codigo != null ? builder.codigo.trim().toUpperCase() : null;
        this.estado = builder.estado;
        this.auditoriaCreacion = builder.auditoriaCreacion;
        this.auditoriaModificacion = builder.auditoriaModificacion;
    }

    /** Único punto de creación/reconstitución: valida el builder antes de construir. Solo Cola lo invoca. */
    protected static Detalle of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    protected void modificar(String nombre, String codigo, Estado estado, Auditoria auditoriaModificacion) {
        if (nombre != null) {
            this.nombre = nombre.trim().toUpperCase();
        }
        if (codigo != null) {
            this.codigo = codigo.trim().toUpperCase();
        }
        if (estado != null) {
            this.estado = estado;
        }
        aplicarAuditoriaModificacion(auditoriaModificacion);
    }

    private void aplicarAuditoriaModificacion(Auditoria auditoriaModificacion) {
        ValidadorNulosVacios
                .variable(auditoriaModificacion, "La auditoria de modificacion del detalle",
                        ColaValidationException::new)
                .noNulo();
        this.auditoriaModificacion = auditoriaModificacion;
    }

    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.nombre, "El nombre del detalle", ColaValidationException::new)
                .noNuloNoVacio();
        ValidadorNulosVacios.variable(builder.codigo, "El codigo del detalle", ColaValidationException::new)
                .noNuloNoVacio();
        if (!builder.codigo.trim().matches("[A-Za-z]{2}")) {
            throw new ColaValidationException("El codigo del detalle debe tener exactamente dos letras");
        }
        ValidadorNulosVacios.variable(builder.estado, "El estado del detalle", ColaValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.auditoriaCreacion, "La auditoria de creacion del detalle",
                        ColaValidationException::new)
                .noNulo();
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

    public Auditoria getAuditoriaCreacion() {
        return auditoriaCreacion;
    }

    public Auditoria getAuditoriaModificacion() {
        return auditoriaModificacion;
    }

    public static class Builder {
        private Long correlativo;
        private String nombre;
        private String codigo;
        private Estado estado;
        private Auditoria auditoriaCreacion;
        private Auditoria auditoriaModificacion;

        public Builder correlativo(Long correlativo) {
            this.correlativo = correlativo;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder codigo(String codigo) {
            this.codigo = codigo;
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

        public String getCodigo() {
            return codigo;
        }

        private Detalle build() {
            return new Detalle(this);
        }
    }
}
