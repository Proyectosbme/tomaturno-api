package com.empresa.tomaturno.estadooperador.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.estadooperador.dominio.exceptions.EstadoOperadorValidationException;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleEstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleTipoDescanso;
import com.empresa.tomaturno.shared.clases.Auditoria;

public class EstadoOperador {

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

    private EstadoOperador() {
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    public static Builder builder() {
        return new Builder();
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

        private Builder() {
        }

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

        /** Para filas nuevas: sin id todavía (lo asigna la persistencia). */
        public EstadoOperador inicializar() {
            EstadoOperador e = construir();
            e.id = null;
            return e;
        }

        /** Para reconstituir desde la base de datos: todos los campos, incluido el id. */
        public EstadoOperador reconstituir() {
            return construir();
        }

        private EstadoOperador construir() {
            EstadoOperador e = new EstadoOperador();
            e.id = this.id;
            e.idUsuario = this.idUsuario;
            e.idSucursal = this.idSucursal;
            e.idPuesto = this.idPuesto;
            e.idEstadoOperador = this.idEstadoOperador;
            e.idTipoDescanso = this.idTipoDescanso;
            e.comentario = this.comentario;
            e.fechaInicio = this.fechaInicio;
            e.fechaFin = this.fechaFin;
            e.auditoria = this.auditoria;
            return e;
        }
    }

    // ─── Factory methods de negocio ──────────────────────────────────────

    /** Activa al operador: queda ACTIVA, disponible para recibir turnos. */
    public static EstadoOperador abrir(Long idUsuario, Long idSucursal, Long idPuesto) {
        EstadoOperador e = nuevaFila(idUsuario, idSucursal, idPuesto, DetalleEstadoOperador.ACTIVA.getValor(), null, null);
        e.validarCreacion();
        return e;
    }

    /**
     * Pone al operador en DESCANSO, con el tipo de descanso indicado. El comentario es
     * obligatorio cuando el tipo es OTRO (el operador explica el motivo); para los demás
     * tipos es opcional.
     */
    public static EstadoOperador iniciarDescanso(Long idUsuario, Long idSucursal, Long idPuesto,
            Long idTipoDescanso, String comentario) {
        EstadoOperador e = nuevaFila(idUsuario, idSucursal, idPuesto, DetalleEstadoOperador.DESCANSO.getValor(),
                idTipoDescanso, comentario);
        e.validarCreacion();
        return e;
    }

    /** Cierra al operador: queda CERRADA, el operador se va. */
    public static EstadoOperador cerrar(Long idUsuario, Long idSucursal, Long idPuesto) {
        EstadoOperador e = nuevaFila(idUsuario, idSucursal, idPuesto, DetalleEstadoOperador.CERRADA.getValor(), null, null);
        e.validarCreacion();
        return e;
    }

    private static EstadoOperador nuevaFila(Long idUsuario, Long idSucursal, Long idPuesto,
            Long idEstadoOperador, Long idTipoDescanso, String comentario) {
        LocalDateTime ahora = LocalDateTime.now();
        return EstadoOperador.builder()
                .idUsuario(idUsuario)
                .idSucursal(idSucursal)
                .idPuesto(idPuesto)
                .idEstadoOperador(idEstadoOperador)
                .idTipoDescanso(idTipoDescanso)
                .comentario(comentario)
                .fechaInicio(ahora)
                .fechaFin(null)
                .auditoria(Auditoria.deCreacion(usuarioAuditoria(idUsuario), ahora))
                .inicializar();
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

    private void validarCreacion() {
        if (this.idUsuario == null)
            throw new EstadoOperadorValidationException("El idUsuario es obligatorio");
        if (this.idSucursal == null)
            throw new EstadoOperadorValidationException("El idSucursal es obligatorio");
        if (this.idEstadoOperador == null)
            throw new EstadoOperadorValidationException("El idEstadoOperador es obligatorio");
        if (this.idEstadoOperador.equals(DetalleEstadoOperador.DESCANSO.getValor()) && this.idTipoDescanso == null)
            throw new EstadoOperadorValidationException(
                    "El idTipoDescanso es obligatorio cuando el operador está en DESCANSO");
        if (this.idTipoDescanso != null && this.idTipoDescanso.equals(DetalleTipoDescanso.OTRO.getValor())
                && (this.comentario == null || this.comentario.isBlank()))
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
