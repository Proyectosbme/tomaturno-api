package com.empresa.tomaturno.cola.dominio.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.cola.dominio.vo.Sucursal;

public class Cola {

    private Long identificador;
    private String nombre;
    private String codigo;
    private Long prioridad;
    private Estado estado;
    private Sucursal sucursal;
    private Auditoria auditoria;
    private List<Detalle> detalles;

    private Cola() {
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long identificador;
        private String nombre;
        private String codigo;
        private Long prioridad;
        private Estado estado;
        private Sucursal sucursal;
        private Auditoria auditoria;
        private List<Detalle> detalles;

        public Builder identificador(Long identificador) {
            this.identificador = identificador;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder prioridad(Long prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Builder estado(Estado estado) {
            this.estado = estado;
            return this;
        }

        public Builder sucursal(Sucursal sucursal) {
            this.sucursal = sucursal;
            return this;
        }

        public Builder auditoria(Auditoria auditoria) {
            this.auditoria = auditoria;
            return this;
        }

        public Builder detalles(List<Detalle> detalles) {
            this.detalles = detalles;
            return this;
        }

        /** Para colas nuevas: sin identificador ni auditoría. */
        public Cola inicializar() {
            Cola c = new Cola();
            c.nombre = this.nombre;
            c.codigo = this.codigo;
            c.prioridad = this.prioridad;
            c.estado = this.estado;
            c.sucursal = this.sucursal;
            return c;
        }

        /** Para reconstituir desde la base de datos: todos los campos. */
        public Cola reconstituir() {
            Cola c = new Cola();
            c.identificador = this.identificador;
            c.nombre = this.nombre;
            c.codigo = this.codigo;
            c.prioridad = this.prioridad;
            c.estado = this.estado;
            c.sucursal = this.sucursal;
            c.auditoria = this.auditoria;
            c.detalles = this.detalles;
            return c;
        }
    }

    /** Referencia ligera (solo id y nombre), sin datos completos. */
    public static Cola referencia(Long id, String nombre) {
        Cola c = new Cola();
        c.identificador = id;
        c.nombre = nombre;
        return c;
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    public void crear(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        validarCreacion();
    }

    public void modificar(String nombre, String codigo, Long prioridad, Estado estado, String usuario) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.auditoria = this.auditoria.conModificacion(usuario, LocalDateTime.now());
        validarModificacion();
    }

    public void agregarDetalle(Detalle detalle) {
        this.detalles.add(detalle);
    }

    public void validarNombreUnico(boolean existeNombreEnSucursal) {
        if (existeNombreEnSucursal) {
            throw new ColaValidationException(
                    "Ya existe una cola con el nombre '" + this.nombre + "' en esta sucursal");
        }
    }

    public void validarNombreDetalleUnico(String nombreDetalle, boolean existeNombreEnCola) {
        if (existeNombreEnCola) {
            throw new ColaValidationException(
                    "Ya existe un detalle con el nombre '" + nombreDetalle + "' en esta cola");
        }
    }

    private void validarCreacion() {
        if (this.nombre == null || this.nombre.isEmpty()) {
            throw new ColaValidationException("El nombre de la cola es obligatorio");
        }
        if (this.codigo == null || this.codigo.isEmpty()) {
            throw new ColaValidationException("El codigo de la cola es obligatorio");
        }
        if (this.prioridad == null) {
            throw new ColaValidationException("La prioridad de la cola es obligatoria");
        }
        if (this.estado == null) {
            throw new ColaValidationException("El estado de la cola es obligatorio");
        }
        if (this.sucursal == null) {
            throw new ColaValidationException("La sucursal de la cola es obligatoria");
        }
    }

    private void validarModificacion() {
        if (this.identificador == null) {
            throw new ColaValidationException("El identificador de la cola es obligatorio");
        }
        validarCreacion();
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public Long getPrioridad() {
        return prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }
}
