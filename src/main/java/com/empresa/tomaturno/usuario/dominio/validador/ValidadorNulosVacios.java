package com.empresa.tomaturno.usuario.dominio.validador;

import java.util.function.Function;

/**
 * Validador fluido genérico de nulos/vacíos. No fija un tipo de excepción propio:
 * cada módulo pasa su propia XxxValidationException (ej. UsuarioValidationException::new)
 * para mantener las excepciones de dominio dentro de su módulo.
 */
public final class ValidadorNulosVacios<T> {

    private final T valor;
    private final String nombreVariable;
    private final Function<String, ? extends RuntimeException> excepcion;

    private ValidadorNulosVacios(T valor, String nombreVariable, Function<String, ? extends RuntimeException> excepcion) {
        this.valor = valor;
        this.nombreVariable = nombreVariable;
        this.excepcion = excepcion;
    }

    public static <T> ValidadorNulosVacios<T> variable(T valor, String nombreVariable,
            Function<String, ? extends RuntimeException> excepcion) {
        return new ValidadorNulosVacios<>(valor, nombreVariable, excepcion);
    }

    public ValidadorNulosVacios<T> noNulo() {
        if (valor == null) {
            throw excepcion.apply(nombreVariable + " no puede ser nulo");
        }
        return this;
    }

    public ValidadorNulosVacios<T> noNuloNoVacio() {
        noNulo();
        if (valor instanceof String texto && texto.isBlank()) {
            throw excepcion.apply(nombreVariable + " no puede estar vacio");
        }
        return this;
    }

    public T obtener() {
        return valor;
    }
}
