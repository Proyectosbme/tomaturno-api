package  com.empresa.tomaturno.catalogos.dominio.exceptions;

public class CatalogoValidationException extends RuntimeException{
    /**
     * Constructor simple con mensaje genérico.
     * 
     * @param mensaje Mensaje descriptivo del error de validación
     */
    public CatalogoValidationException(String mensaje) {
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
    public CatalogoValidationException(String campo, String mensaje) {
        super("Validación fallida en '" + campo + "': " + mensaje);
    }
}