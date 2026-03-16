package com.empresa.tomaturno.sucursal.dominio.vo;

import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;

public class Contacto {
    String telefono;
    String correo;
    String direccion;


    public Contacto() {
    }


    public Contacto(String telefono, String correo, String direccion) {
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public void validar() {
        if (this.telefono == null || this.telefono.isEmpty()) {
            throw new SucursalValidationException("El teléfono no puede ser nulo o vacío");
        }
        if (this.correo == null || this.correo.isEmpty()) {
            throw new SucursalValidationException("El correo no puede ser nulo o vacío");
        }
        if (this.direccion == null || this.direccion.isEmpty()) {
            throw new SucursalValidationException("La dirección no puede ser nula o vacía");
        }
    }

    public String getTelefono() {
        return telefono;
    }


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getCorreo() {
        return correo;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getDireccion() {
        return direccion;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    @Override
    public String toString() {
        return "Contacto [telefono=" + telefono + ", correo=" + correo + ", direccion=" + direccion + "]";
    }
    
    
}
