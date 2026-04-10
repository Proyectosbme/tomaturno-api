package com.empresa.tomaturno.persona.dominio.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.empresa.tomaturno.persona.dominio.exceptions.PersonaValidationException;

public class Persona {

    private Long id;
    private String dui;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;
    private LocalDateTime fechaCreacion;
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

    public static Builder builder() {
        return new Builder();
    }

    /* ── Factory method ───────────────────────────────────────────────── */

    public static Persona inicializar(String dui, String nombres, String apellidos,
            LocalDate fechaNacimiento, String sexo) {
        return builder()
                .dui(dui)
                .nombres(nombres)
                .apellidos(apellidos)
                .fechaNacimiento(fechaNacimiento)
                .sexo(sexo)
                .build();
    }

    /* ── Comportamiento ───────────────────────────────────────────────── */

    public void crear() {
        validarCreacion();
        this.fechaCreacion = LocalDateTime.now();
    }

    public void actualizar(String nombres, String apellidos, LocalDate fechaNacimiento, String sexo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.fechaModificacion = LocalDateTime.now();
    }

    /* ── Validaciones privadas ────────────────────────────────────────── */

    private void validarCreacion() {
        if (this.dui == null || this.dui.isBlank())
            throw new PersonaValidationException("El DUI es obligatorio");
    }

    /* ── Getters ──────────────────────────────────────────────────────── */

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

    /* ── Builder ──────────────────────────────────────────────────────── */

    public static class Builder {

        private Long id;
        private String dui;
        private String nombres;
        private String apellidos;
        private LocalDate fechaNacimiento;
        private String sexo;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaModificacion;

        private Builder() {
        }

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

        public Persona build() {
            return new Persona(this);
        }
    }
}
