package com.empresa.tomaturno.turno.dominio.vo;

public enum DetalleEstado {
    CREADO(1L),
    LLAMADO(2L),
    TRASLADO(3L),
    FINALIZADO(4L),
    SIN_ATENDER(5L),
    EN_ESPERA(6L);

    private final long valor;

    DetalleEstado(long valor) {
        this.valor = valor;
    }

    public long getValor() {
        return valor;
    }
}
