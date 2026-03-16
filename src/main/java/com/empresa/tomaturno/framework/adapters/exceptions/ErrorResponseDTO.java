package com.coop.tomaturno.framework.adapters.exceptions;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de Respuesta de Error: ErrorResponseDTO
 * 
 * Estructura estándar para todas las respuestas de error HTTP.
 * Se utiliza en GlobalExceptionHandler para devolver errores consistentes al cliente.
 * 
 * Responsabilidad: Encapsular información de error en formato JSON estructurado.
 * 
 * Campos:
 * - status: Código HTTP (400, 404, 500, etc.)
 * - error: Categoría del error (Validation Failed, Not Found, etc.)
 * - message: Descripción detallada del error
 * - timestamp: Momento exacto del error (para auditoría)
 * - path: Ruta HTTP donde ocurrió el error (/api/menus, /api/modulos, etc.)
 * - details: Lista opcional de detalles adicionales (errores de validación campo a campo)
 * 
 * Ejemplo JSON:
 * {
 *   "status": 400,
 *   "error": "Validation Failed",
 *   "message": "Nombre del menú no puede estar vacío",
 *   "timestamp": "2024-01-15T10:30:45.123456",
 *   "path": "/api/menus",
 *   "details": ["nombre: no puede estar vacío", "jerarquia: debe ser > 0"]
 * }
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {

    /** Código de estado HTTP (400, 404, 500, etc.) */
    private int status;
    
    /** Categoría del error (ej: "Validation Failed", "Not Found", "Internal Server Error") */
    private String error;
    
    /** Descripción detallada del error para el cliente */
    private String message;
    
    /** Timestamp del momento en que ocurrió el error */
    private LocalDateTime timestamp;
    
    /** Ruta HTTP que causó el error (para debugging) */
    private String path;
    
    /** Lista opcional de detalles (ej: errores de validación por campo) */
    private List<String> details;

    /**
     * Constructor: status, error, message, path
     * El timestamp se asigna automáticamente al momento actual.
     * 
     * @param status Código HTTP
     * @param error Categoría del error
     * @param message Descripción del error
     * @param path Ruta HTTP
     */
    public ErrorResponseDTO(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    /**
     * Constructor: status, error, message, path, details
     * El timestamp se asigna automáticamente al momento actual.
     * 
     * @param status Código HTTP
     * @param error Categoría del error
     * @param message Descripción del error
     * @param path Ruta HTTP
     * @param details Lista de detalles adicionales
     */
    public ErrorResponseDTO(int status, String error, String message, String path, List<String> details) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.details = details;
    }
}
