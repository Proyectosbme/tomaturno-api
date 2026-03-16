package com.coop.tomaturno.turno.dominio.vo;

public enum EstadoTurno {

    CREADO(1, "CREADO"),
    LLAMADO(2, "LLAMADO"),
    TRASLADO(3, "TRASLADO"),
    FINALIZADO(4, "FINALIZADO");

    private final Integer codigo;
    private final String descripcion;

    EstadoTurno(Integer codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }

    public static EstadoTurno fromCodigo(Integer codigo) {
        if (codigo == null) throw new IllegalArgumentException("EstadoTurno no puede ser nulo");
        for (EstadoTurno e : values()) {
            if (e.codigo.equals(codigo)) return e;
        }
        throw new IllegalArgumentException("EstadoTurno inválido: " + codigo + ". Válidos: 1,2,3,4");
    }
}
