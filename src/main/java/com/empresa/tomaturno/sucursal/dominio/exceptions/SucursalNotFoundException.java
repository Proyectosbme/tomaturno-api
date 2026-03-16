package com.empresa.tomaturno.sucursal.dominio.exceptions;

public class SucursalNotFoundException extends RuntimeException{
    /**
     * Constructor simple con mensaje descriptivo.
     * 
     * @param mensaje Mensaje descriptivo de la entidad no encontrada
     */
    public SucursalNotFoundException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con ID y nombre de la entidad.
     * Proporciona contexto específico sobre qué se buscaba.
     * 
     * Ejemplo generado: "Menú con ID 123 no encontrada"
     * 
     * @param id               Identificador de la entidad buscada
     * @param nombreDominio    Nombre descriptivo de la entidad (ej: "Menú", "Perfil", "Módulo")
     */
    public SucursalNotFoundException(Long id,String nombreDominio) {
        super(nombreDominio + " con ID " + id + " no encontrada");
    }
}