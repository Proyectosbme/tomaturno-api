package com.empresa.tomaturno.shared.clases;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Value Object inmutable que encapsula los datos de auditoría de una entidad.
 * Una vez creado no puede ser modificado: agregar modificación retorna una nueva instancia.
 */
public class Auditoria {

    private final String usuarioCreacion;
    private final LocalDateTime fechaCreacion;
    private final String usuarioModificacion;
    private final LocalDateTime fechaModificacion;

    private Auditoria(String usuarioCreacion, LocalDateTime fechaCreacion,
                      String usuarioModificacion, LocalDateTime fechaModificacion) {
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
    }

    // ─── Factory methods ──────────────────────────────────────────────────

    /**
     * Crea una auditoría de creación. Valida que usuario y fecha no sean nulos.
     */
    public static Auditoria deCreacion(String usuario, LocalDateTime fecha) {
        if (usuario == null || usuario.isBlank())
            throw new IllegalArgumentException("El usuario de auditoría no puede estar vacío");
        if (fecha == null)
            throw new IllegalArgumentException("La fecha de creación en auditoria es obligatoria");
        return new Auditoria(usuario, fecha, null, null);
    }

    /**
     * Reconstituye una auditoría desde la base de datos.
     * No valida los campos opcionales de modificación.
     */
    public static Auditoria reconstituir(String usuarioCreacion, LocalDateTime fechaCreacion,
                                         String usuarioModificacion, LocalDateTime fechaModificacion) {
        return new Auditoria(usuarioCreacion, fechaCreacion, usuarioModificacion, fechaModificacion);
    }

    /**
     * Retorna una nueva instancia con los datos de modificación aplicados.
     * El original no se modifica (inmutabilidad).
     */
    public Auditoria conModificacion(String usuario, LocalDateTime fecha) {
        if (usuario == null || usuario.isBlank())
            throw new IllegalArgumentException("El usuario de modificación en auditoria es obligatorio");
        if (fecha == null)
            throw new IllegalArgumentException("La fecha de modificación en auditoria es obligatoria");
        return new Auditoria(this.usuarioCreacion, this.fechaCreacion, usuario, fecha);
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    // ─── equals / hashCode / toString ────────────────────────────────────

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Auditoria other = (Auditoria) obj;
        return Objects.equals(usuarioCreacion, other.usuarioCreacion)
                && Objects.equals(fechaCreacion, other.fechaCreacion)
                && Objects.equals(usuarioModificacion, other.usuarioModificacion)
                && Objects.equals(fechaModificacion, other.fechaModificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioCreacion, fechaCreacion, usuarioModificacion, fechaModificacion);
    }

    @Override
    public String toString() {
        return "Auditoria [usuarioCreacion=" + usuarioCreacion + ", fechaCreacion=" + fechaCreacion
                + ", usuarioModificacion=" + usuarioModificacion + ", fechaModificacion=" + fechaModificacion + "]";
    }
}
