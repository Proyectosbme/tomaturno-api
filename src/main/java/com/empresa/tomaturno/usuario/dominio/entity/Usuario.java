package com.empresa.tomaturno.usuario.dominio.entity;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;
import com.empresa.tomaturno.usuario.dominio.validador.ValidadorNulosVacios;
import com.empresa.tomaturno.usuario.dominio.vo.Auditoria;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

public final class Usuario {

    private Long identificador;
    private final Long idSucursal;
    private Long idPuesto;
    private String codigoUsuario;
    private String contrasena;
    private String keycloakId;
    private Estado estado;
    private Auditoria auditoriaCreacion;
    private Auditoria auditoriaModificacion;
    private DatosPersonales datosPersonales;
    private ConfiguracionOperador configuracion;
    private byte[] foto;
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
        this.auditoriaCreacion = builder.auditoriaCreacion;
        this.auditoriaModificacion = builder.auditoriaModificacion;
        this.datosPersonales = builder.datosPersonales;
        this.configuracion = builder.configuracion;
        this.foto = builder.foto;
        this.nombreSucursal = builder.nombreSucursal;
        this.nombrePuesto = builder.nombrePuesto;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /**
     * Único punto de creación/reconstitución: valida el builder antes de construir.
     * El código de usuario y la auditoría de creación deben venir ya resueltos en el
     * builder (los resuelve el caso de uso antes de llamar a este método: el código
     * requiere consultar unicidad contra el repositorio y la auditoría requiere el
     * actor autenticado, ninguno de los dos es responsabilidad del agregado).
     */
    public static Usuario of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    public void asignarDatosKeycloak(String codigoUsuario, DatosPersonales datosPersonales, String perfil) {
        this.codigoUsuario = codigoUsuario;
        this.datosPersonales = datosPersonales;
        this.configuracion.asignarPerfil(perfil);
    }

    public void asignarNombresKeycloak(String codigoUsuario, DatosPersonales datosPersonales) {
        this.codigoUsuario = codigoUsuario;
        this.datosPersonales = datosPersonales;
    }

    /**
     * Deriva el código de usuario candidato (primera letra del nombre + primer
     * apellido). El caso de uso pide luego al gateway un código único a partir de
     * este candidato antes de estamparlo en el builder.
     */
    public static String generarCodigoUsuario(DatosPersonales datosPersonales) {
        if (datosPersonales == null
                || datosPersonales.getNombres() == null
                || datosPersonales.getApellidos() == null) {
            throw new UsuarioValidationException(
                    "Los datos personales con nombres y apellidos son necesarios para crear el código de usuario");
        }
        String nombres = datosPersonales.getNombres().trim();
        String apellidos = datosPersonales.getApellidos().trim();
        // Primera letra del nombre (en minúscula)
        String primeraLetraNombre = "";
        if (!nombres.equalsIgnoreCase("usuario")) {
            primeraLetraNombre = nombres.substring(0, 1).toLowerCase();
        }
        // Solo el primer apellido (antes del primer espacio)
        String primerApellido = apellidos.split("\\s+")[0].toLowerCase();
        return primeraLetraNombre + primerApellido;
    }

    /**
     * Regla de autorización de creación: solo ADMIN/SUBADMIN pueden crear usuarios.
     * Se valida sobre el string crudo de la petición antes de construir el agregado;
     * "perfilCreador" no es un dato persistido de Usuario, solo un dato transitorio
     * de autorización que vive en el Builder mientras se resuelve la creación.
     */
    public static void validarPerfilCreador(String perfilCreador) {
        if (perfilCreador == null || perfilCreador.isBlank()) {
            throw new UsuarioValidationException("El perfil del usuario creador es obligatorio");
        }
        boolean creadorAutorizado = perfilCreador.equalsIgnoreCase("ADMIN")
                || perfilCreador.equalsIgnoreCase("SUBADMIN");
        if (!creadorAutorizado) {
            throw new UsuarioValidationException("Solo usuarios con perfil ADMIN pueden crear usuarios");
        }
    }

    /** auditoriaModificacion ya viene construida (Auditoria.of(usuario, fecha)); esta entidad no la arma. */
    public void modificar(Long idPuesto, Estado estado, DatosPersonales datosPersonales,
            ConfiguracionOperador configuracion, Auditoria auditoriaModificacion) {

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
        aplicarAuditoriaModificacion(auditoriaModificacion);
        validarModificacion();
    }

    private void aplicarAuditoriaModificacion(Auditoria auditoriaModificacion) {
        ValidadorNulosVacios
                .variable(auditoriaModificacion, "La auditoria de modificacion del usuario",
                        UsuarioValidationException::new)
                .noNulo();
        this.auditoriaModificacion = auditoriaModificacion;
    }

    public void asignarIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public void asignarKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public void asignarCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    /** auditoriaModificacion ya viene construida; esta entidad no arma la fecha/actor. */
    public void asignarFoto(byte[] foto, Auditoria auditoriaModificacion) {
        this.foto = foto;
        aplicarAuditoriaModificacion(auditoriaModificacion);
    }

    /* ── Enriquecimiento ──────────────────────────────────────────────── */

    public void asignarNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public void asignarNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    /* ── Validaciones privadas ────────────────────────────────────────── */

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado
     * inválido. Se aplica tanto a la creación como a la reconstitución desde
     * persistencia (ambas pasan por Usuario.of).
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.codigoUsuario, "El código de usuario", UsuarioValidationException::new)
                .noNuloNoVacio();
        ValidadorNulosVacios.variable(builder.idSucursal, "La sucursal del usuario", UsuarioValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.estado, "El estado del usuario", UsuarioValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.auditoriaCreacion, "La auditoria de creacion del usuario",
                        UsuarioValidationException::new)
                .noNulo();
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

    public Auditoria getAuditoriaCreacion() {
        return auditoriaCreacion;
    }

    public Auditoria getAuditoriaModificacion() {
        return auditoriaModificacion;
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

    public String getCorreo() {
        return datosPersonales != null ? datosPersonales.getCorreo() : null;
    }

    public String getPerfil() {
        return configuracion != null ? configuracion.getPerfil() : null;
    }

    public String getIp() {
        return configuracion != null ? configuracion.getIp() : null;
    }

    public Integer getCorrelativo() {
        return configuracion != null ? configuracion.getCorrelativoPuesto() : null;
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
        private Auditoria auditoriaCreacion;
        private Auditoria auditoriaModificacion;
        private DatosPersonales datosPersonales;
        private ConfiguracionOperador configuracion;
        private byte[] foto;
        private String nombreSucursal;
        private String nombrePuesto;
        /** Dato transitorio de autorización de creación: no forma parte del estado persistido de Usuario. */
        private String perfilCreador;

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

        public Builder auditoriaCreacion(Auditoria auditoriaCreacion) {
            this.auditoriaCreacion = auditoriaCreacion;
            return this;
        }

        public Builder auditoriaModificacion(Auditoria auditoriaModificacion) {
            this.auditoriaModificacion = auditoriaModificacion;
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

        public Builder nombreSucursal(String nombreSucursal) {
            this.nombreSucursal = nombreSucursal;
            return this;
        }

        public Builder nombrePuesto(String nombrePuesto) {
            this.nombrePuesto = nombrePuesto;
            return this;
        }

        public Builder perfilCreador(String perfilCreador) {
            this.perfilCreador = perfilCreador;
            return this;
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

        public DatosPersonales getDatosPersonales() {
            return datosPersonales;
        }

        public ConfiguracionOperador getConfiguracion() {
            return configuracion;
        }

        public String getPerfilCreador() {
            return perfilCreador;
        }

        private Usuario build() {
            return new Usuario(this);
        }
    }
}
