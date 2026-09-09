package com.empresa.tomaturno.puesto.dominio.vo;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Estampa de auditoría (usuario + fecha) de un único evento. Puesto guarda una instancia
 * separada para creación y otra para modificación, en vez de un solo objeto con los
 * cuatro campos combinados.
 */
public class Auditoria {

    private final String usuario;
    private final LocalDateTime fecha;

    private Auditoria(String usuario, LocalDateTime fecha) {
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public static Auditoria of(String usuario, LocalDateTime fecha) {
        if (usuario == null || usuario.isBlank())
            throw new IllegalArgumentException("El usuario de auditoría no puede estar vacío");
        if (fecha == null)
            throw new IllegalArgumentException("La fecha de auditoría es obligatoria");
        return new Auditoria(usuario, fecha);
    }

    public static Auditoria reconstituir(String usuario, LocalDateTime fecha) {
        return usuario == null ? null : new Auditoria(usuario, fecha);
    }

    public String getUsuario() {
        return usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Auditoria other = (Auditoria) obj;
        return Objects.equals(usuario, other.usuario) && Objects.equals(fecha, other.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, fecha);
    }

    @Override
    public String toString() {
        return "Auditoria [usuario=" + usuario + ", fecha=" + fecha + "]";
    }
}
