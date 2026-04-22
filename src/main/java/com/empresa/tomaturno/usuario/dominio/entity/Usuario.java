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
    private String keycloakId;
    private Estado estado;
    private Auditoria auditoria;
    private DatosPersonales datosPersonales;
    private ConfiguracionOperador configuracion;
    private byte[] foto;
    private String perfilCreador;
    private String nombreSucursal;
    private String nombrePuesto;

    private Usuario(Builder builder) {
        this.identificador = builder.identificador;
        this.idSucursal = builder.idSucursal;
        this.idPuesto = builder.idPuesto;
        this.codigoUsuario = builder.codigoUsuario;
        this.contrasena = builder.contrasena;
        this.keycloakId = builder.keycloakId;
        this.estado = builder.estado;
        this.auditoria = builder.auditoria;
        this.datosPersonales = builder.datosPersonales;
        this.configuracion = builder.configuracion;
        this.foto = builder.foto;
    }

    public void asignarDatosKeycloak(String codigoUsuario,
            DatosPersonales datosPersonales, String perfil) {
        this.codigoUsuario = codigoUsuario;
        this.datosPersonales = datosPersonales;
        this.configuracion.asignarPerfil(perfil);
    }

    public void asignarNombresKeycloak(String codigoUsuario, DatosPersonales datosPersonales) {
        this.codigoUsuario = codigoUsuario;
        this.datosPersonales = datosPersonales;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void completarRegistro() {
        this.crearCodigoUsuario();
        this.verificarContrasena();
    }

    private void verificarContrasena() {
        this.contrasena = this.getContrasena() != null && !this.getContrasena().isBlank()
                ? this.getContrasena()
                : this.getCodigoUsuario();
    }
    /* ── Factory methods ──────────────────────────────────────────────── */

    public static Usuario inicializar(Long idSucursal, Long idPuesto, String codigoUsuario,
            Estado estado, DatosPersonales datosPersonales,
            ConfiguracionOperador configuracion) {
        return builder()
                .idSucursal(idSucursal)
                .idPuesto(idPuesto)
                .codigoUsuario(codigoUsuario)
                .estado(estado)
                .datosPersonales(datosPersonales)
                .configuracion(configuracion)
                .build();
    }

    /* ── Comportamiento ───────────────────────────────────────────────── */

    public void crear(String usuarioCreador) {
        this.crearCodigoUsuario();
        this.verificarContrasena();
        this.auditoria = Auditoria.deCreacion(usuarioCreador, LocalDateTime.now());
        this.validarCreacion();
    }

    public void modificar(Long idPuesto,
            Estado estado, DatosPersonales datosPersonales,
            ConfiguracionOperador configuracion, String usuarioModificador) {

        if (idPuesto != null) {
            this.idPuesto = idPuesto;
        }
        if (estado != null) {
            this.estado = estado;
        }

        if (datosPersonales != null) {
            this.datosPersonales = datosPersonales;
        }

        if (configuracion != null) {
            this.configuracion = configuracion;
        }

        if (usuarioModificador != null) {
            this.auditoria = this.auditoria.conModificacion(usuarioModificador, LocalDateTime.now());
        }
        validarModificacion();
    }

    public void asignarIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public void asignarKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    private void crearCodigoUsuario() {
        if (this.datosPersonales == null
                || this.datosPersonales.getNombres() == null
                || this.datosPersonales.getApellidos() == null) {
            throw new UsuarioValidationException(
                    "Los datos personales con nombres y apellidos son necesarios para crear el código de usuario");
        }
        // Quitar espacios al inicio y fin
        String nombres = this.datosPersonales.getNombres().trim();
        String apellidos = this.datosPersonales.getApellidos().trim();
        // Primera letra del nombre (en minúscula)
         String primeraLetraNombre = "";
        if (!nombres.equalsIgnoreCase("usuario")) {
             primeraLetraNombre = nombres.substring(0, 1).toLowerCase();
        }
        // Solo el primer apellido (antes del primer espacio)
        String primerApellido = apellidos.split("\\s+")[0].toLowerCase();
        // Construir el código
        String codigo = primeraLetraNombre + primerApellido;
        this.codigoUsuario = codigo;
    }

    public void asignarCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
        if (this.auditoria == null)
            this.auditoria = Auditoria.deCreacion(this.codigoUsuario, LocalDateTime.now());
    }

    public void asignarFoto(byte[] foto) {
        this.foto = foto;
        this.auditoria = this.auditoria.conModificacion("SISTEMA", LocalDateTime.now());
    }

    /* ── Enriquecimiento ──────────────────────────────────────────────── */

    public void asignarPerfilCreador(String perfilCreador) {
        this.perfilCreador = perfilCreador;
    }

    public void asignarNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public void asignarNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    /* ── Validaciones privadas ────────────────────────────────────────── */

    private void validarCreacion() {
        if (this.codigoUsuario == null || this.codigoUsuario.isBlank())
            throw new UsuarioValidationException("El código de usuario es obligatorio");
        if (this.idSucursal == null)
            throw new UsuarioValidationException("La sucursal es obligatoria");
        if (this.estado == null)
            throw new UsuarioValidationException("El estado es obligatorio");
        if (this.perfilCreador == null || this.perfilCreador.isBlank()) {
            throw new UsuarioValidationException("El perfil del usuario creador es obligatorio");
        }
        boolean creadorAutorizado = this.perfilCreador.equalsIgnoreCase("ADMIN");
        if (!creadorAutorizado) {
            throw new UsuarioValidationException("Solo usuarios con perfil ADMIN pueden crear usuarios");
        }
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

    public String getKeycloakId() {
        return keycloakId;
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
        private String keycloakId;
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

        public Builder keycloakId(String keycloakId) {
            this.keycloakId = keycloakId;
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
