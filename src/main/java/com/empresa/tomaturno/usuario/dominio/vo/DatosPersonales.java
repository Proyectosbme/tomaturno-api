package com.empresa.tomaturno.usuario.dominio.vo;

import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;

public class DatosPersonales {

    private final String nombres;
    private final String apellidos;
    private final String dui;
    private final String telefono;
    private final String correo;

    private DatosPersonales(String nombres, String apellidos, String dui, String telefono, String correo) {
        this.nombres = nombres != null ? nombres.toUpperCase() : null;
        this.apellidos = apellidos != null ? apellidos.toUpperCase() : null;;
        this.dui = dui;
        this.telefono = telefono;
        this.correo = correo;
    }

    public static DatosPersonales crear(String nombres, String apellidos, String dui, String telefono, String correo) {
        if (nombres == null || nombres.isBlank())
            throw new UsuarioValidationException("Los nombres son obligatorios");
        if (apellidos == null || apellidos.isBlank())
            throw new UsuarioValidationException("Los apellidos son obligatorios");
        return new DatosPersonales(
                nombres.trim().toUpperCase(),
                apellidos.trim().toUpperCase(),
                dui != null ? dui.trim().toUpperCase() : null,
                telefono != null ? telefono.trim().toUpperCase() : null,
                correo != null ? correo.trim().toLowerCase() : null);
    }

    public static DatosPersonales reconstituir(String nombres, String apellidos, String dui, String telefono) {
        return new DatosPersonales(nombres, apellidos, dui, telefono, null);
    }

    public static DatosPersonales reconstituir(String nombres, String apellidos, String dui, String telefono, String correo) {
        return new DatosPersonales(nombres, apellidos, dui, telefono, correo);
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDui() {
        return dui;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }
}
