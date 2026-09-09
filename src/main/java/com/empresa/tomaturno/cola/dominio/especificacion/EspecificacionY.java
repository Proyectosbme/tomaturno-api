package com.empresa.tomaturno.cola.dominio.especificacion;

public final class EspecificacionY<T> extends AbstractEspecificacion<T> {

    private final Especificacion<T> especificacion1;
    private final Especificacion<T> especificacion2;

    public EspecificacionY(final Especificacion<T> especificacion1, final Especificacion<T> especificacion2) {
        this.especificacion1 = especificacion1;
        this.especificacion2 = especificacion2;
    }

    @Override
    public boolean esSatisfechaPor(final T t) {
        return especificacion1.esSatisfechaPor(t) && especificacion2.esSatisfechaPor(t);
    }

    @Override
    public void verificar(T t) {
        especificacion1.verificar(t);
        especificacion2.verificar(t);
    }
}
