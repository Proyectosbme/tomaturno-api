package com.empresa.tomaturno.turno.dominio.vo;

public enum CatalogoEstado {
    ESTADO_TURNO(1L);

    private final long valor;

    CatalogoEstado(long valor) { this.valor = valor; }

    public long getValor() { return valor; }
}
