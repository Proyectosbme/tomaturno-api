package  com.coop.tomaturno.cola.dominio.exceptions;

public class ColaValidationException extends RuntimeException{
    /**
     * Constructor simple con mensaje genérico.
     * 
     * @param mensaje Mensaje descriptivo del error de validación
     */
    public ColaValidationException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con campo y mensaje detallado.
     * Proporciona contexto sobre qué campo falló la validación.
     * 
     * Ejemplo: "Validación fallida en 'nombre': Debe estar entre 1 y 100 caracteres"
     * 
     * @param campo   Nombre del campo que falló la validación
     * @param mensaje Descripción del error específico
     */
    public ColaValidationException(String campo, String mensaje) {
        super("Validación fallida en '" + campo + "': " + mensaje);
    }
}