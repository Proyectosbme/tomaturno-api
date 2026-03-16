package com.empresa.tomaturno.framework.adapters.exceptions;

/**
 * Excepción de Error Interno: InternalServerException
 * 
 * Excepción runtime que representa un error interno del servidor.
 * Mapeada a HTTP 500 Internal Server Error por GenericExceptionMapper en GlobalExceptionHandler.
 * 
 * Caso de uso: Errores inesperados en lógica de negocio que no son validación o no encontrado.
 * 
 * Ejemplos:
 * - Fallo en cálculo de jerarquía
 * - Inconsistencia de datos en BD
 * - Error en integraciones externas
 * - Condiciones de carrera en actualización
 * 
 * Responsabilidad: Indicar que ocurrió un error imprevisto que requiere investigación.
 * El mensaje debe ser descriptivo para logs pero seguro para exposición al cliente.
 */
public class InternalServerException extends RuntimeException {

    /**
     * Constructor con mensaje descriptivo.
     * 
     * @param message Descripción del error interno
     *                Ejemplo: "No se pudo actualizar la jerarquía de menús"
     */
    public InternalServerException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     * Preserva la excepción original para debugging.
     * 
     * @param message Descripción del error interno
     * @param cause Excepción subyacente que causó este error
     */
    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
