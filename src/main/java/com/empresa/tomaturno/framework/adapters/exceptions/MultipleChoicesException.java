package com.empresa.tomaturno.framework.adapters.exceptions;

/**
 * Excepción de Múltiples Opciones: MultipleChoicesException
 * 
 * Excepción runtime que indica que existen múltiples opciones disponibles.
 * Mapeada a HTTP 300 Multiple Choices por mapper personalizado en GlobalExceptionHandler.
 * 
 * Caso de uso: Cuando una búsqueda o operación retorna múltiples resultados válidos
 * pero se esperaba un único resultado (ambigüedad).
 * 
 * Ejemplo:
 * - Búsqueda de menú por nombre incompleto retorna 3 coincidencias
 * - Menú padre ambiguo en estructura jerárquica
 * 
 * Responsabilidad: Indicar que se requiere aclaración/selección del cliente.
 */
public class MultipleChoicesException extends RuntimeException {

    /**
     * Constructor con mensaje descriptivo.
     * 
     * @param message Descripción de las opciones disponibles
     *                Ejemplo: "Se encontraron 3 menús que coinciden con el criterio"
     */
    public MultipleChoicesException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Descripción de las opciones disponibles
     * @param cause Excepción que causó este error (para debugging)
     */
    public MultipleChoicesException(String message, Throwable cause) {
        super(message, cause);
    }
}
