package com.empresa.tomaturno.catalogos.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

public class CatalogoDetalle {

    private Integer correlativo;
    private String nombre;
    private String descripcion;
    private Estado estado;
    private Auditoria auditoria;

    private CatalogoDetalle(String nombre, String descripcion, Estado estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public static CatalogoDetalle inicializar(String nombre, String descripcion) {
        return new CatalogoDetalle(nombre, descripcion, Estado.ACTIVO);
    }

    public void crear(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        this.validarCreacion();
    }

    public static CatalogoDetalle reconstruirCatalogoDetalle(String nombre, String descripcion, Estado estado,
            Auditoria auditoria) {
        CatalogoDetalle detalle = new CatalogoDetalle(nombre, descripcion, estado);
        detalle.auditoria = auditoria;
        return detalle;
    }

    public static CatalogoDetalle conCorrelativo(Integer correlativo) {
        CatalogoDetalle d = new CatalogoDetalle(null, null, null);
        d.correlativo = correlativo;
        return d;
    }

    public void asignarCorrelativo(Integer correlativo) {
        this.correlativo = correlativo;
    }

    private void validarCreacion() {
        if(this.correlativo == null)
            throw new IllegalArgumentException("El correlativo no puede ser nulo");
        if (this.nombre == null || this.nombre.isEmpty())
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        if (this.auditoria == null)
            throw new IllegalArgumentException("La auditoria no puede ser nula");
        if (estado == null)
            throw new IllegalArgumentException("El estado no puede ser nulo");
    }

    public Integer getCorrelativo() {
        return correlativo;
    }

    public String getNombre() {
        return nombre;
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
}
