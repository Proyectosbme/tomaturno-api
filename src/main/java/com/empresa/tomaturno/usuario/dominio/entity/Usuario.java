package com.empresa.tomaturno.usuario.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

public class Usuario {

    private Long identificador;
    private Long idSucursal;
    private Long idPuesto;
    private String codigoUsuario;
    private String contrasena;
    private Estado estado;
    private Auditoria auditoria;
    private DatosPersonales datosPersonales;
    private ConfiguracionOperador configuracion;
    private byte[] foto;
    // Campos enriquecidos (no persistidos)
    private String perfilCreador;
    private String nombreSucursal;
    private String nombrePuesto;

    private Usuario(Builder builder) {
        this.identificador = builder.identificador;
        this.idSucursal = builder.idSucursal;
        this.idPuesto = builder.idPuesto;
        this.codigoUsuario = builder.codigoUsuario;
        this.contrasena = builder.contrasena;
        this.estado = builder.estado;
        this.auditoria = builder.auditoria;
        this.datosPersonales = builder.datosPersonales;
        this.configuracion = builder.configuracion;
        this.foto = builder.foto;
    }

    public static Builder builder() {
        return new Builder();
    }

    /* ── Factory methods ──────────────────────────────────────────────── */

    public static Usuario inicializar(Long idSucursal, Long idPuesto, String codigoUsuario,
            String contrasena, Estado estado, DatosPersonales datosPersonales,
            ConfiguracionOperador configuracion) {
        return builder()
                .idSucursal(idSucursal)
                .idPuesto(idPuesto)
                .codigoUsuario(codigoUsuario)
                .contrasena(contrasena)
                .estado(estado)
                .datosPersonales(datosPersonales)
                .configuracion(configuracion)
                .build();
    }

    /* ── Comportamiento ───────────────────────────────────────────────── */

    public void crear(String usuarioCreador) {
        this.auditoria = Auditoria.deCreacion(usuarioCreador, LocalDateTime.now());
        validarCreacion();
    }

    public void modificar(Long idPuesto, String codigoUsuario, String contrasena,
            Estado estado, DatosPersonales datosPersonales,
            ConfiguracionOperador configuracion, String usuarioModificador) {
        this.idPuesto = idPuesto;
        this.codigoUsuario = codigoUsuario;
        if (contrasena != null && !contrasena.isBlank()) {
            this.contrasena = contrasena;
        }
        this.estado = estado;
        this.datosPersonales = datosPersonales;
        this.configuracion = configuracion;
        this.auditoria = this.auditoria.conModificacion(usuarioModificador, LocalDateTime.now());
        validarModificacion();
    }

    public void asignarIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public void asignarContrasenaHasheada(String hash) {
        this.contrasena = hash;
    }

    public void validarCodigoUnico(boolean existeCodigo) {
        if (existeCodigo)
            throw new UsuarioValidationException(
                    "Ya existe un usuario con el código '" + this.codigoUsuario + "' en esta sucursal");
    }

    public void asignarFoto(byte[] foto) {
        this.foto = foto;
        this.auditoria = this.auditoria.conModificacion("SISTEMA", LocalDateTime.now());
    }

    /* ── Enriquecimiento ──────────────────────────────────────────────── */

    public void asignarPerfilCreador(String perfilCreador) {
        this.perfilCreador = perfilCreador;
    }

    public void enriquecerNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public void enriquecerNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    /* ── Validaciones privadas ────────────────────────────────────────── */

    private void validarCreacion() {
        if (this.codigoUsuario == null || this.codigoUsuario.isBlank())
            throw new UsuarioValidationException("El código de usuario es obligatorio");
        if (this.contrasena == null || this.contrasena.isBlank())
            throw new UsuarioValidationException("La contraseña es obligatoria");
        if (this.idSucursal == null)
            throw new UsuarioValidationException("La sucursal es obligatoria");
        if (this.estado == null)
            throw new UsuarioValidationException("El estado es obligatorio");
    }

    private void validarModificacion() {
        if (this.identificador == null)
            throw new UsuarioValidationException("El identificador del usuario es obligatorio");
        if (this.codigoUsuario == null || this.codigoUsuario.isBlank())
            throw new UsuarioValidationException("El código de usuario es obligatorio");
        if (this.estado == null)
            throw new UsuarioValidationException("El estado es obligatorio");
    }

    /* ── Getters ──────────────────────────────────────────────────────── */

    public Long getIdentificador() {
        return identificador;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Estado getEstado() {
        return estado;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public DatosPersonales getDatosPersonales() {
        return datosPersonales;
    }

    public ConfiguracionOperador getConfiguracion() {
        return configuracion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public String getPerfilCreador() {
        return perfilCreador;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    /* ── Delegate getters (compatibilidad con mappers) ────────────────── */

    public String getNombres() {
        return datosPersonales != null ? datosPersonales.getNombres() : null;
    }

    public String getApellidos() {
        return datosPersonales != null ? datosPersonales.getApellidos() : null;
    }

    public String getDui() {
        return datosPersonales != null ? datosPersonales.getDui() : null;
    }

    public String getTelefono() {
        return datosPersonales != null ? datosPersonales.getTelefono() : null;
    }

    public String getPerfil() {
        return configuracion != null ? configuracion.getPerfil() : null;
    }

    public String getIp() {
        return configuracion != null ? configuracion.getIp() : null;
    }

    public Integer getCorrelativo() {
        return configuracion != null ? configuracion.getCorrelativo() : null;
    }

    public Integer getAtenderCasosEspeciales() {
        return configuracion != null ? configuracion.getAtenderCasosEspeciales() : null;
    }

    /* ── Builder ──────────────────────────────────────────────────────── */

    public static class Builder {

        private Long identificador;
        private Long idSucursal;
        private Long idPuesto;
        private String codigoUsuario;
        private String contrasena;
        private Estado estado;
        private Auditoria auditoria;
        private DatosPersonales datosPersonales;
        private ConfiguracionOperador configuracion;
        private byte[] foto;

        private Builder() {
        }

        public Builder identificador(Long identificador) {
            this.identificador = identificador;
            return this;
        }

        public Builder idSucursal(Long idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public Builder idPuesto(Long idPuesto) {
            this.idPuesto = idPuesto;
            return this;
        }

        public Builder codigoUsuario(String codigoUsuario) {
            this.codigoUsuario = codigoUsuario;
            return this;
        }

        public Builder contrasena(String contrasena) {
            this.contrasena = contrasena;
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

        public Builder datosPersonales(DatosPersonales datosPersonales) {
            this.datosPersonales = datosPersonales;
            return this;
        }

        public Builder configuracion(ConfiguracionOperador configuracion) {
            this.configuracion = configuracion;
            return this;
        }

        public Builder foto(byte[] foto) {
            this.foto = foto;
            return this;
        }

        public Usuario build() {
            return new Usuario(this);
        }
    }
}
