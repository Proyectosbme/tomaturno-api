package com.empresa.tomaturno.empresa.dominio.entity;

public class Empresa {

    private static final Long ID_FIJO = 1L;

    private Long id;
    private String nombre;
    private byte[] banner;
    private byte[] logo;

    private Empresa(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.banner = builder.banner;
        this.logo = builder.logo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Empresa reconstituir(Long id, String nombre, byte[] banner, byte[] logo) {
        return builder()
                .id(id)
                .nombre(nombre)
                .banner(banner)
                .logo(logo)
                .build();
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

        private Builder() {
        }

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

        public Empresa build() {
            return new Empresa(this);
        }
    }
}
