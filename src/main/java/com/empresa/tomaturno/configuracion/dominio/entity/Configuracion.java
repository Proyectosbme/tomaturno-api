package com.empresa.tomaturno.configuracion.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.configuracion.dominio.exceptions.ConfiguracionValidationException;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

public class Configuracion {

    private Long idConfiguracion;
    private Long idSucursal;
    private String nombre;
    private Integer parametro;
    private String valorTexto;
    private String descripcion;
    private Estado estado;
    private Auditoria auditoria;
    private String nombreSucursal;

    private Configuracion() {
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long idConfiguracion;
        private Long idSucursal;
        private String nombre;
        private Integer parametro;
        private String valorTexto;
        private String descripcion;
        private Estado estado;
        private Auditoria auditoria;
        private String nombreSucursal;

        public Builder idConfiguracion(Long idConfiguracion) {
            this.idConfiguracion = idConfiguracion;
            return this;
        }

        public Builder idSucursal(Long idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder parametro(Integer parametro) {
            this.parametro = parametro;
            return this;
        }

        public Builder valorTexto(String valorTexto) {
            this.valorTexto = valorTexto;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder estado(Estado estado) {
            this.estado = estado;
            return this;
        }

        public Builder auditoria(Auditoria auditoria) {
            this.auditoria = auditoria;
            return this;
        }

        public Builder nombreSucursal(String nombreSucursal) {
            this.nombreSucursal = nombreSucursal;
            return this;
        }

        /** Para configuraciones nuevas: sin id ni auditoría. */
        public Configuracion inicializar() {
            Configuracion c = new Configuracion();
            c.idSucursal = this.idSucursal;
            c.nombre = this.nombre;
            c.parametro = this.parametro;
            c.valorTexto = this.valorTexto;
            c.descripcion = this.descripcion;
            c.estado = this.estado;
            return c;
        }

        /** Para reconstituir desde la base de datos: todos los campos. */
        public Configuracion reconstituir() {
            Configuracion c = new Configuracion();
            c.idConfiguracion = this.idConfiguracion;
            c.idSucursal = this.idSucursal;
            c.nombre = this.nombre;
            c.parametro = this.parametro;
            c.valorTexto = this.valorTexto;
            c.descripcion = this.descripcion;
            c.estado = this.estado;
            c.auditoria = this.auditoria;
            c.nombreSucursal = this.nombreSucursal;
            return c;
        }
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    public void crear(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        validarCreacion();
    }

    public void modificar(String nombre, Integer parametro, String valorTexto,
            String descripcion, Estado estado, String usuario) {
        this.nombre = nombre;
        this.parametro = parametro;
        this.valorTexto = valorTexto;
        this.descripcion = descripcion;
        this.estado = estado;
        this.auditoria = this.auditoria.conModificacion(usuario, LocalDateTime.now());
        validarModificacion();
    }

    private void validarCreacion() {
        if (this.nombre == null || this.nombre.isBlank()) {
            throw new ConfiguracionValidationException("El nombre de la configuración es obligatorio");
        }
        if (this.idSucursal == null) {
            throw new ConfiguracionValidationException("La sucursal es obligatoria");
        }
        if (this.estado == null) {
            throw new ConfiguracionValidationException("El estado es obligatorio");
        }
    }

    private void validarModificacion() {
        if (this.idConfiguracion == null) {
            throw new ConfiguracionValidationException("El identificador de la configuración es obligatorio");
        }
        if (this.nombre == null || this.nombre.isBlank()) {
            throw new ConfiguracionValidationException("El nombre de la configuración es obligatorio");
        }
        if (this.estado == null) {
            throw new ConfiguracionValidationException("El estado es obligatorio");
        }
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getIdConfiguracion() {
        return idConfiguracion;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getParametro() {
        return parametro;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }
}
