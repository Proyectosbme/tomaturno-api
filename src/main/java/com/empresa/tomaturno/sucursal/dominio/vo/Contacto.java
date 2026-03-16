package com.empresa.tomaturno.sucursal.dominio.vo;

import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;

public class Contacto {

    private final String telefono;
    private final String correo;
    private final String direccion;

    private Contacto(String telefono, String correo, String direccion) {
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public static Contacto crear(String telefono, String correo, String direccion) {
        if (telefono == null || telefono.isBlank()) {
            throw new SucursalValidationException("El teléfono no puede ser nulo o vacío");
        }
        if (correo == null || correo.isBlank()) {
            throw new SucursalValidationException("El correo no puede ser nulo o vacío");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new SucursalValidationException("La dirección no puede ser nula o vacía");
        }
        return new Contacto(telefono, correo, direccion);
    }

    public static Contacto reconstituir(String telefono, String correo, String direccion) {
        return new Contacto(telefono, correo, direccion);
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Contacto [telefono=" + telefono + ", correo=" + correo + ", direccion=" + direccion + "]";
    }
}
