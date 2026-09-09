package com.empresa.tomaturno.persona.dominio.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.empresa.tomaturno.persona.dominio.exceptions.PersonaValidationException;
import com.empresa.tomaturno.persona.dominio.validador.ValidadorNulosVacios;

/**
 * A diferencia de Cola/Configuracion, la tabla persona no registra "quién" crea o modifica
 * (no hay columnas de usuario), solo "cuándo": por eso no existe un VO Auditoria local aquí,
 * solo fechaCreacion/fechaModificacion, estampadas por el llamador antes de construir/modificar.
 */
public final class Persona {

    private final Long id;
    private final String dui;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    private Persona(Builder builder) {
        this.id = builder.id;
        this.dui = builder.dui;
        this.nombres = builder.nombres;
        this.apellidos = builder.apellidos;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.sexo = builder.sexo;
        this.fechaCreacion = builder.fechaCreacion;
        this.fechaModificacion = builder.fechaModificacion;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /** Único punto de creación/reconstitución: valida el builder antes de construir. */
    public static Persona of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    /** fechaModificacion ya viene estampada por el llamador; esta entidad no la arma. */
    public void modificar(String nombres, String apellidos, LocalDate fechaNacimiento, String sexo,
            LocalDateTime fechaModificacion) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        aplicarFechaModificacion(fechaModificacion);
    }

    private void aplicarFechaModificacion(LocalDateTime fechaModificacion) {
        ValidadorNulosVacios
                .variable(fechaModificacion, "La fecha de modificacion de la persona",
                        PersonaValidationException::new)
                .noNulo();
        this.fechaModificacion = fechaModificacion;
    }

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado inválido.
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.dui, "El DUI de la persona", PersonaValidationException::new)
                .noNuloNoVacio();
        ValidadorNulosVacios
                .variable(builder.fechaCreacion, "La fecha de creacion de la persona",
                        PersonaValidationException::new)
                .noNulo();
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public String getDui() {
        return dui;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public static class Builder {
        private Long id;
        private String dui;
        private String nombres;
        private String apellidos;
        private LocalDate fechaNacimiento;
        private String sexo;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaModificacion;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder dui(String dui) {
            this.dui = dui;
            return this;
        }

        public Builder nombres(String nombres) {
            this.nombres = nombres;
            return this;
        }

        public Builder apellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }

        public Builder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public Builder sexo(String sexo) {
            this.sexo = sexo;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder fechaModificacion(LocalDateTime fechaModificacion) {
            this.fechaModificacion = fechaModificacion;
            return this;
        }

        private Persona build() {
            return new Persona(this);
        }
    }
}
