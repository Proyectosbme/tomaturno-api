package com.empresa.tomaturno.cola.dominio.validador;

import java.math.BigDecimal;
import java.util.function.Function;

/** Validador fluido genérico de números. Ver {@link ValidadorNulosVacios} para el porqué de recibir la excepción. */
public final class ValidadorNumeros<T extends Number> {

    private final T valor;
    private final String nombreVariable;
    private final Function<String, ? extends RuntimeException> excepcion;

    private ValidadorNumeros(T valor, String nombreVariable, Function<String, ? extends RuntimeException> excepcion) {
        this.valor = valor;
        this.nombreVariable = nombreVariable;
        this.excepcion = excepcion;
    }

    public static <T extends Number> ValidadorNumeros<T> variable(T valor, String nombreVariable,
            Function<String, ? extends RuntimeException> excepcion) {
        return new ValidadorNumeros<>(valor, nombreVariable, excepcion);
    }

    private ValidadorNumeros<T> noNulo() {
        ValidadorNulosVacios.variable(valor, nombreVariable, excepcion).noNulo();
        return this;
    }

    public ValidadorNumeros<T> positivo() {
        noNulo();
        if (new BigDecimal(valor.toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw excepcion.apply(nombreVariable + " debe ser mayor que cero");
        }
        return this;
    }

    public ValidadorNumeros<T> negativo() {
        noNulo();
        if (new BigDecimal(valor.toString()).compareTo(BigDecimal.ZERO) >= 0) {
            throw excepcion.apply(nombreVariable + " debe ser menor que cero");
        }
        return this;
    }

    public ValidadorNumeros<T> cero() {
        noNulo();
        if (new BigDecimal(valor.toString()).compareTo(BigDecimal.ZERO) != 0) {
            throw excepcion.apply(nombreVariable + " debe ser igual a cero");
        }
        return this;
    }

    public T obtener() {
        return valor;
    }
}
