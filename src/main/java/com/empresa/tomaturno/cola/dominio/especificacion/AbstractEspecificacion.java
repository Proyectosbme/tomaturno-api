package com.empresa.tomaturno.cola.dominio.especificacion;

public abstract class AbstractEspecificacion<T> implements Especificacion<T> {

    @Override
    public Especificacion<T> y(final Especificacion<T> especificacion) {
        return new EspecificacionY<>(this, especificacion);
    }
}
