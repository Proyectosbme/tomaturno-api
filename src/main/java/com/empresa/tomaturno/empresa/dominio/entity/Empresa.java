package com.empresa.tomaturno.empresa.dominio.entity;

import com.empresa.tomaturno.empresa.dominio.exceptions.EmpresaValidationException;
import com.empresa.tomaturno.empresa.dominio.validador.ValidadorNulosVacios;

public final class Empresa {

    private static final Long ID_FIJO = 1L;

    private final Long id;
    private String nombre;
    private byte[] banner;
    private byte[] logo;

    private Empresa(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.banner = builder.banner;
        this.logo = builder.logo;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /** Único punto de creación/reconstitución: valida el builder antes de construir. */
    public static Empresa of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    public static Long idFijo() {
        return ID_FIJO;
    }

    /* ── Comportamiento ───────────────────────────────────────────────── */

    public void actualizarNombre(String nombre) {
        this.nombre = nombre;
    }

    public void actualizarBanner(byte[] banner) {
        this.banner = banner;
    }

    public void actualizarLogo(byte[] logo) {
        this.logo = logo;
    }

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado
     * inválido. Empresa es un registro único (id fijo); nombre/banner/logo pueden
     * no estar definidos aún (antes de la primera actualización).
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.id, "El identificador de la empresa", EmpresaValidationException::new)
                .noNulo();
    }

    /* ── Getters ──────────────────────────────────────────────────────── */

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public byte[] getBanner() {
        return banner;
    }

    public byte[] getLogo() {
        return logo;
    }

    /* ── Builder ──────────────────────────────────────────────────────── */

    public static class Builder {

        private Long id;
        private String nombre;
        private byte[] banner;
        private byte[] logo;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder banner(byte[] banner) {
            this.banner = banner;
            return this;
        }

        public Builder logo(byte[] logo) {
            this.logo = logo;
            return this;
        }

        private Empresa build() {
            return new Empresa(this);
        }
    }
}
