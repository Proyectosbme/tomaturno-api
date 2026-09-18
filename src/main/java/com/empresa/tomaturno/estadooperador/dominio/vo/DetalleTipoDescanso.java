package com.empresa.tomaturno.estadooperador.dominio.vo;

public enum DetalleTipoDescanso {
    BANO(1L),
    COMIDA(2L),
    OTRO(3L),
    /** Descanso automático (no seleccionable por el operador): se asigna cuando se detecta
     *  que la caja quedó ACTIVA sin ninguna sesión de WebSocket conectada por más de 30s.
     *  Ver EstadoOperadorAutomaticoOrquestador. */
    SESION_CERRADA(5L);

    private final long valor;

    DetalleTipoDescanso(long valor) {
        this.valor = valor;
    }

    public long getValor() {
        return valor;
    }
}
