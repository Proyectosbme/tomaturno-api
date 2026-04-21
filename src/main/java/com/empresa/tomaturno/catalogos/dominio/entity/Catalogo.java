package com.empresa.tomaturno.catalogos.dominio.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.catalogos.dominio.exceptions.CatalogoValidationException;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

public class Catalogo {
    private Integer identificador;
    private String nombre;
    private String descripcion;
    private List<CatalogoDetalle> detalles;
    private Auditoria auditoria;
    private Estado estado;

    private Catalogo(Integer identificador, String nombre, String descripcion, Estado estado,
            List<CatalogoDetalle> detalles) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.detalles = detalles;
    }

    public static Catalogo inicializar(Integer identificador, String nombre, String descripcion) {
        return new Catalogo(identificador, nombre, descripcion, Estado.ACTIVO, new ArrayList<>());
    }

    public void crear(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        this.validarCreacion();
    }

    public static Catalogo reconstruirCatalogo(Integer identificador, String nombre,
            String descripcion, Estado estado, List<CatalogoDetalle> detalles, Auditoria auditoria) {
        Catalogo catalogo = new Catalogo(identificador, nombre, descripcion, estado, detalles);
        catalogo.auditoria = auditoria;
        return catalogo;
    }

    public void asignarIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    private void validarCreacion() {
        if (this.nombre == null || this.nombre.isEmpty())
            throw new CatalogoValidationException("El nombre no puede ser nulo o vacío");
        if (this.auditoria == null)
            throw new CatalogoValidationException("La auditoria no puede ser nula");
        if (estado == null)
            throw new CatalogoValidationException("El estado no puede ser nulo");
    }

    

    public void agregarDetalle( CatalogoDetalle detalle) {
        this.detalles.add(detalle);
    }

    public void cargarDetalles(List<CatalogoDetalle> detalles) {
        this.detalles.addAll(detalles);
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<CatalogoDetalle> getDetalles() {
        return detalles;
    }

}
