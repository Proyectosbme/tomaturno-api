package com.empresa.tomaturno.cola.dominio.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;
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

    public Cola() {
    }

    public Cola(String nombre, String codigo, Long prioridad, Estado estado, Sucursal sucursal, Auditoria auditoria) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.sucursal = sucursal;
        this.auditoria = auditoria;
    }

    public Cola(Long identificador, String nombre, String codigo, Long prioridad, Estado estado, Sucursal sucursal,
            Auditoria auditoria) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.codigo = codigo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.sucursal = sucursal;
        this.auditoria = auditoria;
    }

    public void agregarDetalle(Detalle detalle) {
        this.detalles.add(detalle);
    }

    public void modificar(String nombre, String codigo, Long prioridad, Estado estado) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    /**
     * Valida que no exista ya una cola con el mismo nombre en la misma sucursal.
     * Se llama ANTES de persistir (crear o modificar).
     */
    public void validarNombreUnico(boolean existeNombreEnSucursal) {
        if (existeNombreEnSucursal) {
            throw new ColaValidationException(
                    "Ya existe una cola con el nombre '" + this.nombre + "' en esta sucursal");
        }
    }

    /**
     * Valida que no exista ya un detalle con el mismo nombre dentro de esta cola.
     * Se llama ANTES de persistir un nuevo detalle.
     */
    public void validarNombreDetalleUnico(String nombreDetalle, boolean existeNombreEnCola) {
        if (existeNombreEnCola) {
            throw new ColaValidationException(
                    "Ya existe un detalle con el nombre '" + nombreDetalle + "' en esta cola");
        }
    }

    public void validarCreacion() {
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
        auditoria.validarCreacion();
    }

    public void validarModificacion() {
        if (this.identificador == null) {
            throw new ColaValidationException("El identificador de la cola es obligatorio");
        }
        this.validarCreacion();
        auditoria.validarModificacion();
    }

    public void crearSucursal(Long identificado, String nombre) {
        this.sucursal = new Sucursal(identificado, nombre);
    }

    public void auditoriaCreacion(String usuarioCreacion, LocalDateTime fechaCreacion) {
        if (this.auditoria == null) {
            this.auditoria = new Auditoria();
        }
        this.auditoria.creacion(usuarioCreacion, fechaCreacion);
    }

    public void auditoriaModificacion(String usuarioModificacion, LocalDateTime fechaModificacion) {
        if (this.auditoria == null) {
            throw new ColaValidationException("No tiene usuario de creacion y modificacion revisar ");
        }
        this.auditoria.modificacion(usuarioModificacion, fechaModificacion);
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Long prioridad) {
        this.prioridad = prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }
}
