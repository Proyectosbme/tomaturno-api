package com.empresa.tomaturno.cola.dominio.especificacion;

public interface Especificacion<T> {

    boolean esSatisfechaPor(T t);

    void verificar(T t);

    Especificacion<T> y(Especificacion<T> especificacion);
}
