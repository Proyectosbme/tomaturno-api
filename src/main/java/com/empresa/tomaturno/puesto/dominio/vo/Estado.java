package com.empresa.tomaturno.puesto.dominio.vo;

public enum Estado {

    ACTIVO(1, "ACTIVO"),
    INACTIVO(0, "INACTIVO");

    private final Integer codigo;
    private final String descripcion;

    Estado(Integer codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static Estado fromCodigo(Integer codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Estado no puede ser nulo o vacío");
        }
        for (Estado estado : values()) {
            if (estado.codigo.compareTo(codigo) == 0) {
                return estado;
            }
        }
        throw new IllegalArgumentException(
                "Estado inválido: '" + codigo + "'. Valores válidos: 1, 0");
    }

    public static Estado fromDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del estado no puede ser nula o vacía");
        }
        String descNorm = quitarTildes(descripcion.trim()).toUpperCase();
        for (Estado estado : values()) {
            if (estado.name().equals(descNorm)) {
                return estado;
            }
        }
        throw new IllegalArgumentException(
                "Estado inválido: '" + descripcion + "'. Valores válidos: ACTIVO, INACTIVO");
    }

    private static String quitarTildes(String input) {
        String norm = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        return norm.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
