package com.empresa.tomaturno.estadooperador.dominio.vo;

public enum DetalleEstadoOperador {
    ACTIVA(1L),
    DESCANSO(2L),
    CERRADA(3L);

    private final long valor;

    DetalleEstadoOperador(long valor) {
        this.valor = valor;
    }

    public long getValor() {
        return valor;
    }
}
