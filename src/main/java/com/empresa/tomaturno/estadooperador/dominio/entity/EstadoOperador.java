package com.empresa.tomaturno.estadooperador.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.estadooperador.dominio.exceptions.EstadoOperadorValidationException;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleEstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleTipoDescanso;
import com.empresa.tomaturno.shared.clases.Auditoria;

public class EstadoOperador {

    private final Long id;
    private final Long idUsuario;
    private final Long idSucursal;
    private final Long idPuesto;
    private final Long idEstadoOperador;
    private final Long idTipoDescanso;
    private final String comentario;
    private final LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Auditoria auditoria;

    private EstadoOperador(Builder builder) {
        this.id = builder.id;
        this.idUsuario = builder.idUsuario;
        this.idSucursal = builder.idSucursal;
        this.idPuesto = builder.idPuesto;
        this.idEstadoOperador = builder.idEstadoOperador;
        this.idTipoDescanso = builder.idTipoDescanso;
        this.comentario = builder.comentario;
        this.fechaInicio = builder.fechaInicio;
        this.fechaFin = builder.fechaFin;
        this.auditoria = builder.auditoria;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /**
     * Único punto de creación/reconstitución: valida el builder antes de construir.
     */
    public static EstadoOperador of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    public static class Builder {
        private Long id;
        private Long idUsuario;
        private Long idSucursal;
        private Long idPuesto;
        private Long idEstadoOperador;
        private Long idTipoDescanso;
        private String comentario;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private Auditoria auditoria;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder idUsuario(Long idUsuario) {
            this.idUsuario = idUsuario;
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

        public Builder idEstadoOperador(Long idEstadoOperador) {
            this.idEstadoOperador = idEstadoOperador;
            return this;
        }

        public Builder idTipoDescanso(Long idTipoDescanso) {
            this.idTipoDescanso = idTipoDescanso;
            return this;
        }

        public Builder comentario(String comentario) {
            this.comentario = comentario;
            return this;
        }

        public Builder fechaInicio(LocalDateTime fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public Builder fechaFin(LocalDateTime fechaFin) {
            this.fechaFin = fechaFin;
            return this;
        }

        public Builder auditoria(Auditoria auditoria) {
            this.auditoria = auditoria;
            return this;
        }

        private EstadoOperador build() {
            return new EstadoOperador(this);
        }
    }

    // ─── Factory methods de negocio ──────────────────────────────────────

    /** Activa al operador: queda ACTIVA, disponible para recibir turnos. */
    public static EstadoOperador abrir(Long idUsuario, Long idSucursal, Long idPuesto) {
        return nuevaFila(idUsuario, idSucursal, idPuesto, DetalleEstadoOperador.ACTIVA.getValor(), null, null);
    }

    /**
     * Pone al operador en DESCANSO, con el tipo de descanso indicado. El comentario es
     * obligatorio cuando el tipo es OTRO (el operador explica el motivo); para los demás
     * tipos es opcional.
     */
    public static EstadoOperador iniciarDescanso(Long idUsuario, Long idSucursal, Long idPuesto,
            Long idTipoDescanso, String comentario) {
        return nuevaFila(idUsuario, idSucursal, idPuesto, DetalleEstadoOperador.DESCANSO.getValor(),
                idTipoDescanso, comentario);
    }

    /** Cierra al operador: queda CERRADA, el operador se va. */
    public static EstadoOperador cerrar(Long idUsuario, Long idSucursal, Long idPuesto) {
        return nuevaFila(idUsuario, idSucursal, idPuesto, DetalleEstadoOperador.CERRADA.getValor(), null, null);
    }

    private static EstadoOperador nuevaFila(Long idUsuario, Long idSucursal, Long idPuesto,
            Long idEstadoOperador, Long idTipoDescanso, String comentario) {
        LocalDateTime ahora = LocalDateTime.now();
        return EstadoOperador.of(new Builder()
                .idUsuario(idUsuario)
                .idSucursal(idSucursal)
                .idPuesto(idPuesto)
                .idEstadoOperador(idEstadoOperador)
                .idTipoDescanso(idTipoDescanso)
                .comentario(comentario)
                .fechaInicio(ahora)
                .fechaFin(null)
                .auditoria(Auditoria.deCreacion(usuarioAuditoria(idUsuario), ahora)));
    }

    /**
     * No existe un usuario "actor" distinto del propio operador para estas acciones
     * (solo el operador puede cambiar su propio estado), así que se usa su id como
     * identificador de auditoría.
     */
    private static String usuarioAuditoria(Long idUsuario) {
        return idUsuario != null ? String.valueOf(idUsuario) : "sistema";
    }

    /**
     * Cierra la vigencia de esta fila (deja de ser el estado actual del operador),
     * marcando fechaFin y el usuario/fecha de modificación.
     */
    public void cerrarVigencia() {
        this.fechaFin = LocalDateTime.now();
        if (this.auditoria != null) {
            this.auditoria = this.auditoria.conModificacion(usuarioAuditoria(this.idUsuario), this.fechaFin);
        }
    }

    // ─── Validaciones ─────────────────────────────────────────────────────

    private static void validarCreacion(Builder builder) {
        if (builder.idUsuario == null)
            throw new EstadoOperadorValidationException("El idUsuario es obligatorio");
        if (builder.idSucursal == null)
            throw new EstadoOperadorValidationException("El idSucursal es obligatorio");
        if (builder.idEstadoOperador == null)
            throw new EstadoOperadorValidationException("El idEstadoOperador es obligatorio");
        if (builder.idEstadoOperador.equals(DetalleEstadoOperador.DESCANSO.getValor()) && builder.idTipoDescanso == null)
            throw new EstadoOperadorValidationException(
                    "El idTipoDescanso es obligatorio cuando el operador está en DESCANSO");
        if (builder.idTipoDescanso != null && builder.idTipoDescanso.equals(DetalleTipoDescanso.OTRO.getValor())
                && (builder.comentario == null || builder.comentario.isBlank()))
            throw new EstadoOperadorValidationException(
                    "El comentario es obligatorio cuando el tipo de descanso es OTRO");
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public Long getIdEstadoOperador() {
        return idEstadoOperador;
    }

    public Long getIdTipoDescanso() {
        return idTipoDescanso;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }
}
