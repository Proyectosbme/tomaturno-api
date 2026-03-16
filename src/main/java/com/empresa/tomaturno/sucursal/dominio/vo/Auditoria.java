package com.empresa.tomaturno.sucursal.dominio.vo;

import java.time.LocalDateTime;
import java.util.Objects;

import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;

public class Auditoria {

    private final String usuarioCreacion;
    private final LocalDateTime fechaCreacion;
    private final String usuarioModificacion;
    private final LocalDateTime fechaModificacion;

    private Auditoria(String usuarioCreacion, LocalDateTime fechaCreacion, String usuarioModificacion,
            LocalDateTime fechaModificacion) {
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
    }

    public static Auditoria deCreacion(String usuarioCreacion, LocalDateTime fechaCreacion) {
        if (usuarioCreacion == null || usuarioCreacion.isBlank()) {
            throw new SucursalValidationException("El usuario de crecion es obligatorio");
        }

        if (fechaCreacion == null) {
            throw new SucursalValidationException("La fecha de creacion es necesario");
        }
        return new Auditoria(usuarioCreacion, fechaCreacion, null, null);
    }

    public static Auditoria reconstituir(String usuarioCreacion, LocalDateTime fechaCreacion,
            String usuarioModificacion, LocalDateTime fechaModificacion) {
        return new Auditoria(usuarioCreacion, fechaCreacion, usuarioModificacion, fechaModificacion);
    }

    public Auditoria conModificacion(String usuario, LocalDateTime fecha) {
        if (usuario == null || usuario.isBlank())
            throw new SucursalValidationException("El usuario de modificación es obligatorio");
        if (fecha == null)
            throw new SucursalValidationException("La fecha de modificación es obligatoria");
        return new Auditoria(this.usuarioCreacion, this.fechaCreacion, usuario, fecha);
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(usuarioCreacion, fechaCreacion, usuarioModificacion, fechaModificacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Auditoria other)) return false;
        return Objects.equals(usuarioCreacion, other.usuarioCreacion)
                && Objects.equals(fechaCreacion, other.fechaCreacion)
                && Objects.equals(usuarioModificacion, other.usuarioModificacion)
                && Objects.equals(fechaModificacion, other.fechaModificacion);
    }

}
