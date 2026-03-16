package com.coop.tomaturno.usuario.dominio.vo;

public enum Estado {

    ACTIVO(1, "ACTIVO"),
    INACTIVO(0, "INACTIVO");

    private final Integer codigo;
    private final String descripcion;

    Estado(Integer codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }

    public static Estado fromCodigo(Integer codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Estado no puede ser nulo");
        }
        for (Estado estado : values()) {
            if (estado.codigo.equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado inválido: '" + codigo + "'. Valores válidos: 1, 0");
    }
}
