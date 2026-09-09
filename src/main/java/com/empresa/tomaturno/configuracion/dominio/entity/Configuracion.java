package com.empresa.tomaturno.configuracion.dominio.entity;

import com.empresa.tomaturno.configuracion.dominio.exceptions.ConfiguracionValidationException;
import com.empresa.tomaturno.configuracion.dominio.validador.ValidadorNulosVacios;
import com.empresa.tomaturno.configuracion.dominio.vo.Auditoria;
import com.empresa.tomaturno.configuracion.dominio.vo.Estado;

public final class Configuracion {

    private final Long idConfiguracion;
    private final Long idSucursal;
    private String nombre;
    private Integer parametro;
    private String descripcion;
    private Estado estado;
    private Auditoria auditoriaCreacion;
    private Auditoria auditoriaModificacion;
    private final String nombreSucursal;

    private Configuracion(Builder builder) {
        this.idConfiguracion = builder.idConfiguracion;
        this.idSucursal = builder.idSucursal;
        this.nombre = builder.nombre;
        this.parametro = builder.parametro;
        this.descripcion = builder.descripcion;
        this.estado = builder.estado;
        this.auditoriaCreacion = builder.auditoriaCreacion;
        this.auditoriaModificacion = builder.auditoriaModificacion;
        this.nombreSucursal = builder.nombreSucursal;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /** Único punto de creación/reconstitución: valida el builder antes de construir. */
    public static Configuracion of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    /** auditoriaModificacion ya viene construida (Auditoria.of(usuario, fecha)); esta entidad no la arma. */
    public void modificar(Integer parametro, String descripcion, Estado estado, Auditoria auditoriaModificacion) {
        this.parametro = parametro;
        this.descripcion = descripcion;
        if (estado != null) {
            this.estado = estado;
        }
        aplicarAuditoriaModificacion(auditoriaModificacion);
    }

    private void aplicarAuditoriaModificacion(Auditoria auditoriaModificacion) {
        ValidadorNulosVacios
                .variable(auditoriaModificacion, "La auditoria de modificacion de la configuracion",
                        ConfiguracionValidationException::new)
                .noNulo();
        this.auditoriaModificacion = auditoriaModificacion;
    }

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado inválido.
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.nombre, "El nombre de la configuracion", ConfiguracionValidationException::new)
                .noNuloNoVacio();
        ValidadorNulosVacios.variable(builder.idSucursal, "La sucursal de la configuracion", ConfiguracionValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.estado, "El estado de la configuracion", ConfiguracionValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.auditoriaCreacion, "La auditoria de creacion de la configuracion",
                        ConfiguracionValidationException::new)
                .noNulo();
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getIdConfiguracion() {
        return idConfiguracion;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getParametro() {
        return parametro;
    }

    public String getDescripcion() {
        return descripcion;
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

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public static class Builder {
        private Long idConfiguracion;
        private Long idSucursal;
        private String nombre;
        private Integer parametro;
        private String descripcion;
        private Estado estado;
        private Auditoria auditoriaCreacion;
        private Auditoria auditoriaModificacion;
        private String nombreSucursal;

        public Builder idConfiguracion(Long idConfiguracion) {
            this.idConfiguracion = idConfiguracion;
            return this;
        }

        public Builder idSucursal(Long idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder parametro(Integer parametro) {
            this.parametro = parametro;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
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

        public Builder nombreSucursal(String nombreSucursal) {
            this.nombreSucursal = nombreSucursal;
            return this;
        }

        private Configuracion build() {
            return new Configuracion(this);
        }
    }
}
