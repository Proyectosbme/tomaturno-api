package com.coop.tomaturno.framework.adapters.exceptions;

/**
 * Excepción de Recurso No Encontrado: NotFoundException
 * 
 * Excepción runtime que representa un recurso no encontrado.
 * Mapeada a HTTP 404 por PersonaNotFoundExceptionMapper en GlobalExceptionHandler.
 * 
 * Responsabilidad: Indicar que una entidad solicitada no existe en la BD.
 * 
 * Uso:
 * - Se lanza en repositorios cuando no se encuentra una entidad por ID
 * - Se propaga hasta GlobalExceptionHandler
 * - Se convierte a HTTP 404 + ErrorResponseDTO
 * 
 * Ejemplo:
 * repository.findById(999)
 *   .orElseThrow(() -> new NotFoundException("Menú con ID 999 no existe"))
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje descriptivo.
     * 
     * @param message Descripción del recurso no encontrado
     *                Ejemplo: "Menú con ID 999 no existe"
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Descripción del recurso no encontrado
     * @param cause Excepción que causó este error (para debugging)
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
