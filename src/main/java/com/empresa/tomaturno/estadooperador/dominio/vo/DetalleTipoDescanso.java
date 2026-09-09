package com.empresa.tomaturno.estadooperador.dominio.vo;

public enum DetalleTipoDescanso {
    BANO(1L),
    COMIDA(2L),
    OTRO(3L);

    private final long valor;

    DetalleTipoDescanso(long valor) {
        this.valor = valor;
    }

    public long getValor() {
        return valor;
    }
}
