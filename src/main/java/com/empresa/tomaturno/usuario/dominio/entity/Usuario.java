package com.empresa.tomaturno.usuario.dominio.entity;

import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;
import com.empresa.tomaturno.usuario.dominio.vo.Auditoria;
import com.empresa.tomaturno.usuario.dominio.vo.Estado;

public class Usuario {

    private Long identificador;
    private Long idSucursal;
    private Long idPuesto;
    private String codigoUsuario;
    private String contrasena;
    private String perfil;
    private String nombres;
    private String apellidos;
    private String dui;
    private Estado estado;
    private String telefono;
    private String ip;
    private Auditoria auditoria;
    private Integer correlativo; // Nuevo campo para el número de puesto dentro de la sucursal
    private Integer atenderCasosEspeciales;
    // Campos enriquecidos (no persistidos)
    private String nombreSucursal;
    private String nombrePuesto;
    private String perfilCreador;

    public Usuario() {
    }

    public void modificar(String codigoUsuario, String contrasena, Long idPuesto,
            String nombres, String apellidos, String dui,
            Estado estado, String telefono, String ip, String perfil, Integer correlativo,
            Integer atenderCasosEspeciales) {
        this.codigoUsuario = codigoUsuario;
        if (contrasena != null && !contrasena.isBlank()) {
            this.contrasena = contrasena;
        }
        this.idPuesto = idPuesto;
        this.correlativo = correlativo;
        this.atenderCasosEspeciales = atenderCasosEspeciales;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dui = dui;
        this.estado = estado;
        this.telefono = telefono;
        this.ip = ip;
        this.perfil = perfil;
    }

    public void auditoriaCreacion(String usuario, java.time.LocalDateTime fecha) {
        if (this.auditoria == null)
            this.auditoria = new Auditoria();
        this.auditoria.creacion(usuario, fecha);
    }

    public void auditoriaModificacion(String usuario, java.time.LocalDateTime fecha) {
        if (this.auditoria == null) {
            throw new UsuarioValidationException("No tiene auditoría de creación");
        }
        this.auditoria.modificacion(usuario, fecha);
    }

    public void validarCreacion() {
        if (this.codigoUsuario == null || this.codigoUsuario.isBlank()) {
            throw new UsuarioValidationException("El código de usuario es obligatorio");
        }
        if (this.contrasena == null || this.contrasena.isBlank()) {
            throw new UsuarioValidationException("La contraseña es obligatoria");
        }
        if (this.nombres == null || this.nombres.isBlank()) {
            throw new UsuarioValidationException("Los nombres son obligatorios");
        }
        if (this.apellidos == null || this.apellidos.isBlank()) {
            throw new UsuarioValidationException("Los apellidos son obligatorios");
        }
        if (this.idSucursal == null) {
            throw new UsuarioValidationException("La sucursal es obligatoria");
        }
        if (this.estado == null) {
            throw new UsuarioValidationException("El estado es obligatorio");
        }
        if (this.perfil == null || this.perfil.isBlank()) {
            throw new UsuarioValidationException("El perfil es obligatorio");
        }
        if ("OPERADOR".equals(this.perfil) && this.correlativo == null) {
            throw new UsuarioValidationException("El número de estación es obligatorio para el perfil Operador");
        }
        auditoria.validarCreacion();
    }

    public void validarModificacion() {
        if (this.identificador == null) {
            throw new UsuarioValidationException("El identificador del usuario es obligatorio");
        }
        if (this.codigoUsuario == null || this.codigoUsuario.isBlank()) {
            throw new UsuarioValidationException("El código de usuario es obligatorio");
        }
        if (this.nombres == null || this.nombres.isBlank()) {
            throw new UsuarioValidationException("Los nombres son obligatorios");
        }
        if (this.apellidos == null || this.apellidos.isBlank()) {
            throw new UsuarioValidationException("Los apellidos son obligatorios");
        }
        if (this.estado == null) {
            throw new UsuarioValidationException("El estado es obligatorio");
        }
        if (this.perfil == null || this.perfil.isBlank()) {
            throw new UsuarioValidationException("El perfil es obligatorio");
        }
        if ("OPERADOR".equals(this.perfil) && this.correlativo == null) {
            throw new UsuarioValidationException("El número de estación es obligatorio para el perfil Operador");
        }

        auditoria.validarModificacion();
    }

    public void validarCodigoUnico(boolean existeCodigo) {
        if (existeCodigo) {
            throw new UsuarioValidationException(
                    "Ya existe un usuario con el código '" + this.codigoUsuario + "' en esta sucursal");
        }
    }

    public Long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Long idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getPerfilCreador() {
        return perfilCreador;
    }

    public void setPerfilCreador(String perfilCreador) {
        this.perfilCreador = perfilCreador;
    }

    public Integer getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(Integer correlativo) {
        this.correlativo = correlativo;
    }

    public Integer getAtenderCasosEspeciales() {
        return atenderCasosEspeciales;
    }

    public void setAtenderCasosEspeciales(Integer atenderCasosEspeciales) {
        this.atenderCasosEspeciales = atenderCasosEspeciales;
    }

}
