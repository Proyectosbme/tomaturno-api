package com.coop.tomaturno.puesto.dominio.vo;

public class Sucursal {

    Long identificador;
    String nombre;

    public Sucursal() {
    }

    public Sucursal(Long identificador, String nombre) {
        this.identificador = identificador;
        this.nombre = nombre;
    }

    public Long getIdentificador() { return identificador; }
    public void setIdentificador(Long identificador) { this.identificador = identificador; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
