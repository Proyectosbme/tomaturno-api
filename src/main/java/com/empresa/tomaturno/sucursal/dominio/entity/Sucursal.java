package com.empresa.tomaturno.sucursal.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;
import com.empresa.tomaturno.sucursal.dominio.vo.Estado;

/**
 * Entidad que representa una sucursal en el sistema de toma de turnos.
 * Contiene información básica de la sucursal, su estado y auditoría.
 * Incluye validaciones para asegurar la integridad de los datos al crear o
 * modificar una sucursal.
 * 
 * Campos:
 * - identificador: ID único de la sucursal (Long)
 * - nombre: Nombre de la sucursal (String)
 * - contacto: Información de contacto de la sucursal (Contacto)
 * - estado: Estado de la sucursal (Estado)
 * - auditoria: Información de auditoría para creación y modificación
 * (Auditoria)
 * 
 * Validaciones:
 * - El nombre no puede ser nulo o vacío.
 * - El contacto no puede ser nulo.
 * - El estado no puede ser nulo.
 * 
 * Auditoría:
 * - Al crear una sucursal, se registra el usuario y fecha de creación.
 * - Al modificar una sucursal, se registra el usuario y fecha de modificación.
 * - Para modificar una sucursal, debe existir previamente una auditoría de
 * creación.
 * 
 * Ejemplo de uso:
 * Sucursal sucursal = new Sucursal("Sucursal Central", contacto, Estado.ACTIVO,
 * auditoria);
 * sucursal.validarCreacion(); // Valida los campos antes de guardar
 * sucursal.auditoriaCreacion("admin", "2024-06-01");
 * 
 * Autores: Bryan Ivan Marroquin Escalante
 * Fecha de creación: 2026-02-10
 * Correos de contacto:bryanivan21165@gmail.com
 * Versión: 1.0
 * Ultima modificación: 2026-02-10
 * 
 */
public class Sucursal {
    /** Identificador único de la sucursal */
    Long identificador;
    /** Nombre de la sucursal */
    String nombre;
    /** Contacto de la sucursal */
    Contacto contacto;
    /** Estado de la sucursal */
    Estado estado;
    /** Auditoria de la sucursal */
    Auditoria auditoria;

    public Sucursal() {
    }

    /**
     * Constructor completo con todos los campos, incluyendo el identificador.
     * 
     * @param identificador
     * @param nombre
     * @param contacto
     * @param estado
     * @param auditoria
     */
    public Sucursal(Long identificador, String nombre, Contacto contacto, Estado estado, Auditoria auditoria) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.contacto = contacto;
        this.estado = estado;
        this.auditoria = auditoria;
    }

    /**
     * 
     * Constructor sin identificador, para creación de nuevas sucursales donde el ID
     * se genera automáticamente.
     * 
     * @param nombre
     * @param contacto
     * @param estado
     * @param auditoria
     * 
     * 
     */
    public Sucursal(String nombre, Contacto contacto, Estado estado, Auditoria auditoria) {
        this.nombre = nombre;
        this.contacto = contacto;
        this.estado = estado;
        this.auditoria = auditoria;
    }

    public void modificar(String nombre, Contacto contacto, Estado estado) {
        this.nombre = nombre;
        this.contacto = contacto;
        this.estado = estado;
    }

    /**
     * Registra la auditoría de creación de la sucursal.
     * 
     * @param usuarioCreacion
     * @param fechaCreacion
     */
    public void auditoriaCreacion(String usuarioCreacion, LocalDateTime fechaCreacion) {
        if (this.auditoria == null) {
            this.auditoria = new Auditoria();
        }
        this.auditoria.creacion(usuarioCreacion, fechaCreacion);
    }


    /**
     * Registra la auditoría de modificación de la sucursal.
     * @param usuarioModificacion
     * @param fechaModificacion
     */
    public void auditoriaModificacion(String usuarioModificacion, LocalDateTime fechaModificacion) {
        this.auditoria.modificacion(usuarioModificacion, fechaModificacion);
    }

    /**
     * Valida los campos necesarios para la creación de una sucursal.
     * Lanza SucursalValidationException si algún campo es inválido.
     * 
     * Campos validados:
     * - nombre: no puede ser nulo o vacío
     * - contacto: no puede ser nulo
     * - estado: no puede ser nulo
     */
    public void validarCreacion() {

        if (this.nombre == null || this.nombre.isBlank()) {
            throw new SucursalValidationException("El nombre de la sucursal no puede ser nulo o vacío");
        }
        if (this.contacto == null) {
            throw new SucursalValidationException("El contacto de la sucursal no puede ser nulo");
        }
        if (this.estado == null) {
            throw new SucursalValidationException("El estado de la sucursal no puede ser nulo");
        }
        this.auditoria.validarCreacion();
        this.contacto.validar();
    }

    public void validarModificacion() {
        if (this.identificador == null) {
            throw new SucursalValidationException(
                    "El identificador de la sucursal no debe ser nulo al modificar una sucursal existente");
        }
        this.auditoria.validarModificacion();
        this.contacto.validar();
    }


    public Long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

}
