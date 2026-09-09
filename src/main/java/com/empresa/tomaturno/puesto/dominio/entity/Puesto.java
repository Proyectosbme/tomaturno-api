package com.empresa.tomaturno.puesto.dominio.entity;

import com.empresa.tomaturno.puesto.dominio.exceptions.PuestoValidationException;
import com.empresa.tomaturno.puesto.dominio.validador.ValidadorNulosVacios;
import com.empresa.tomaturno.puesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.puesto.dominio.vo.Estado;
import com.empresa.tomaturno.puesto.dominio.vo.Sucursal;

public final class Puesto {

    private final Long identificador;
    private String nombre;
    private String nombreLlamada;
    private Estado estado;
    private final Sucursal sucursal;
    private Auditoria auditoriaCreacion;
    private Auditoria auditoriaModificacion;

    private Puesto(Builder builder) {
        this.identificador = builder.identificador;
        this.nombre = builder.nombre != null ? builder.nombre.trim().toUpperCase() : null;
        this.nombreLlamada = builder.nombreLlamada != null ? builder.nombreLlamada.trim().toUpperCase() : null;
        this.estado = builder.estado;
        this.sucursal = builder.sucursal;
        this.auditoriaCreacion = builder.auditoriaCreacion;
        this.auditoriaModificacion = builder.auditoriaModificacion;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /**
     * Único punto de creación/reconstitución: valida el builder antes de construir.
     */
    public static Puesto of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    /**
     * auditoriaModificacion ya viene construida (Auditoria.of(usuario, fecha));
     * esta entidad no la arma.
     */
    public void modificar(String nombre, String nombreLlamada, Estado estado, Auditoria auditoriaModificacion) {
        if (nombre != null) {
            this.nombre = nombre.trim().toUpperCase();
        }
        if (nombreLlamada != null) {
            this.nombreLlamada = nombreLlamada.trim().toUpperCase();
        }
        if (estado != null) {
            this.estado = estado;
        }
        aplicarAuditoriaModificacion(auditoriaModificacion);
    }

    private void aplicarAuditoriaModificacion(Auditoria auditoriaModificacion) {
        ValidadorNulosVacios
                .variable(auditoriaModificacion, "La auditoria de modificacion del puesto",
                        PuestoValidationException::new)
                .noNulo();
        this.auditoriaModificacion = auditoriaModificacion;
    }

    public void validarNombreUnico(boolean existeNombreEnSucursal) {
        if (existeNombreEnSucursal) {
            throw new PuestoValidationException(
                    "Ya existe un puesto con el nombre '" + this.nombre + "' en esta sucursal");
        }
    }

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado
     * inválido.
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.nombre, "El nombre del puesto", PuestoValidationException::new)
                .noNuloNoVacio();
        ValidadorNulosVacios.variable(builder.estado, "El estado del puesto", PuestoValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.sucursal, "La sucursal del puesto", PuestoValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.auditoriaCreacion, "La auditoria de creacion del puesto",
                        PuestoValidationException::new)
                .noNulo();
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

    public Auditoria getAuditoriaCreacion() {
        return auditoriaCreacion;
    }

    public Auditoria getAuditoriaModificacion() {
        return auditoriaModificacion;
    }

    public static class Builder {
        private Long identificador;
        private String nombre;
        private String nombreLlamada;
        private Estado estado;
        private Sucursal sucursal;
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

        public Builder nombreLlamada(String nombreLlamada) {
            this.nombreLlamada = nombreLlamada;
            return this;
        }

        public Builder estado(Estado estado) {
            this.estado = estado;
            return this;
        }

        public Builder sucursal(Sucursal sucursal) {
            this.sucursal = sucursal;
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

        private Puesto build() {
            return new Puesto(this);
        }
    }
}
