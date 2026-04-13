package com.empresa.tomaturno.turno.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;
import com.empresa.tomaturno.turno.dominio.vo.EstadoTurno;

public class Turno {

    private Long id; // referencia Long auto-generada (para idTurnoRelacionado)
    private Long idSucursal;
    private LocalDateTime fechaCreacion;
    private String codigoTurno;
    private LocalDateTime fechaLlamada;
    private LocalDateTime fechaFinalizacion;
    private Long idCola;
    private Long idDetalle;
    private EstadoTurno estado;
    private Long idTurnoRelacionado; // id del turno original al reasignar
    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idUsuario;
    private Long idPersona;
    private Integer tipoCasoEspecial;
    // Campo enriquecido (no persistido)
    private String nombreLlamada;

    private Turno(Builder builder) {
        this.id = builder.id;
        this.idSucursal = builder.idSucursal;
        this.fechaCreacion = builder.fechaCreacion;
        this.codigoTurno = builder.codigoTurno;
        this.fechaLlamada = builder.fechaLlamada;
        this.fechaFinalizacion = builder.fechaFinalizacion;
        this.idCola = builder.idCola;
        this.idDetalle = builder.idDetalle;
        this.estado = builder.estado;
        this.idTurnoRelacionado = builder.idTurnoRelacionado;
        this.idPuesto = builder.idPuesto;
        this.idSucursalPuesto = builder.idSucursalPuesto;
        this.idUsuario = builder.idUsuario;
        this.idPersona = builder.idPersona;
        this.tipoCasoEspecial = builder.tipoCasoEspecial;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Turno inicializar(Long idSucursal, Long idCola, Long idDetalle,
            String codigoTurno, Long idPersona, Integer tipoCasoEspecial) {
        Turno turno = Turno.builder()
                .idSucursal(idSucursal)
                .idCola(idCola)
                .idDetalle(idDetalle)
                .codigoTurno(codigoTurno)
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoTurno.CREADO)
                .idPersona(idPersona)
                .tipoCasoEspecial(tipoCasoEspecial)
                .build();
        turno.validarCreacion();
        return turno;
    }

    /* ── Asignación de ID generado por secuencia ──── */

    public void asignarId(Long id) {
        this.id = id;
    }

    /* ── Comportamiento del dominio ──────────────── */

    public void llamar(Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        validarTransicionLlamar(idPuesto, idSucursalPuesto);
        this.idPuesto = idPuesto;
        this.idSucursalPuesto = idSucursalPuesto;
        this.idUsuario = idUsuario;
        this.fechaLlamada = LocalDateTime.now();
        this.estado = EstadoTurno.LLAMADO;
    }

    public void rellamar() {
        this.fechaLlamada = LocalDateTime.now();
    }

    public Turno reasignarA(Long nuevoId, Long idSucursalDestino, Long idColaDestino, Long idDetalleValido) {
        validarTransicionReasignar();
        this.estado = EstadoTurno.TRASLADO;
        return Turno.builder()
                .id(nuevoId)
                .idSucursal(idSucursalDestino)
                .idCola(idColaDestino)
                .idDetalle(idDetalleValido)
                .codigoTurno(this.codigoTurno)
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoTurno.CREADO)
                .idTurnoRelacionado(this.id)
                .build();
    }

    /**
     * Re-llama un turno ya finalizado o trasladado (desde historial del operador)
     */
    public void rellamarDesdeHistorial(Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        if (estado != EstadoTurno.FINALIZADO && estado != EstadoTurno.TRASLADO)
            throw new TurnoValidationException(
                    "Solo se puede re-llamar un turno en estado FINALIZADO o TRASLADO. Estado actual: " + estado);
        this.idPuesto = idPuesto;
        this.idSucursalPuesto = idSucursalPuesto;
        this.idUsuario = idUsuario;
        this.fechaLlamada = LocalDateTime.now();
        this.estado = EstadoTurno.LLAMADO;
    }

    public void sinAtender() {
        if (estado != EstadoTurno.LLAMADO)
            throw new TurnoValidationException(
                    "Solo se puede marcar sin atender un turno en estado LLAMADO. Estado actual: " + estado);
        this.estado = EstadoTurno.SIN_ATENDER;
    }

    public void finalizar() {
        validarTransicionFinalizar();
        this.fechaFinalizacion = LocalDateTime.now();
        this.estado = EstadoTurno.FINALIZADO;
    }

    public void enriquecerNombreLlamada(String nombreLlamada) {
        this.nombreLlamada = nombreLlamada;
    }

    /* ── Validaciones privadas ────────────────────── */

    private void validarCreacion() {
        if (idSucursal == null)
            throw new TurnoValidationException("idSucursal es obligatorio");
        if (idCola == null)
            throw new TurnoValidationException("idCola es obligatorio");
        if (codigoTurno == null || codigoTurno.isBlank())
            throw new TurnoValidationException("codigoTurno es obligatorio");
        if (estado == null)
            throw new TurnoValidationException("estado es obligatorio");
    }

    private void validarTransicionLlamar(Long idPuesto, Long idSucursalPuesto) {
        if (estado != EstadoTurno.CREADO)
            throw new TurnoValidationException(
                    "Solo se puede llamar un turno en estado CREADO. Estado actual: " + estado);
        if (idPuesto == null)
            throw new TurnoValidationException("idPuesto es obligatorio para llamar un turno");
        if (idSucursalPuesto == null)
            throw new TurnoValidationException("idSucursalPuesto es obligatorio para llamar un turno");
    }

    private void validarTransicionReasignar() {
        if (estado != EstadoTurno.LLAMADO)
            throw new TurnoValidationException(
                    "Solo se puede reasignar un turno en estado LLAMADO. Estado actual: " + estado);
    }

    private void validarTransicionFinalizar() {
        if (estado != EstadoTurno.LLAMADO && estado != EstadoTurno.TRASLADO)
            throw new TurnoValidationException(
                    "Solo se puede finalizar un turno en estado LLAMADO o TRASLADO. Estado actual: " + estado);
    }

    /* ── Getters ─────────────────────────────────── */

    public Long getId() {
        return id;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getCodigoTurno() {
        return codigoTurno;
    }

    public LocalDateTime getFechaLlamada() {
        return fechaLlamada;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public Long getIdCola() {
        return idCola;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public Long getIdTurnoRelacionado() {
        return idTurnoRelacionado;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public Long getIdSucursalPuesto() {
        return idSucursalPuesto;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public Integer getTipoCasoEspecial() {
        return tipoCasoEspecial;
    }

    public String getNombreLlamada() {
        return nombreLlamada;
    }

    /* ── Builder ─────────────────────────────────── */

    public static class Builder {
        private Long id;
        private Long idSucursal;
        private LocalDateTime fechaCreacion;
        private String codigoTurno;
        private LocalDateTime fechaLlamada;
        private LocalDateTime fechaFinalizacion;
        private Long idCola;
        private Long idDetalle;
        private EstadoTurno estado;
        private Long idTurnoRelacionado;
        private Long idPuesto;
        private Long idSucursalPuesto;
        private Long idUsuario;
        private Long idPersona;
        private Integer tipoCasoEspecial;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder idSucursal(Long idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder codigoTurno(String codigoTurno) {
            this.codigoTurno = codigoTurno;
            return this;
        }

        public Builder fechaLlamada(LocalDateTime fechaLlamada) {
            this.fechaLlamada = fechaLlamada;
            return this;
        }

        public Builder fechaFinalizacion(LocalDateTime fechaFinalizacion) {
            this.fechaFinalizacion = fechaFinalizacion;
            return this;
        }

        public Builder idCola(Long idCola) {
            this.idCola = idCola;
            return this;
        }

        public Builder idDetalle(Long idDetalle) {
            this.idDetalle = idDetalle;
            return this;
        }

        public Builder estado(EstadoTurno estado) {
            this.estado = estado;
            return this;
        }

        public Builder idTurnoRelacionado(Long idTurnoRelacionado) {
            this.idTurnoRelacionado = idTurnoRelacionado;
            return this;
        }

        public Builder idPuesto(Long idPuesto) {
            this.idPuesto = idPuesto;
            return this;
        }

        public Builder idSucursalPuesto(Long idSucursalPuesto) {
            this.idSucursalPuesto = idSucursalPuesto;
            return this;
        }

        public Builder idUsuario(Long idUsuario) {
            this.idUsuario = idUsuario;
            return this;
        }

        public Builder idPersona(Long idPersona) {
            this.idPersona = idPersona;
            return this;
        }

        public Builder tipoCasoEspecial(Integer tipoCasoEspecial) {
            this.tipoCasoEspecial = tipoCasoEspecial;
            return this;
        }

        public Turno build() {
            return new Turno(this);
        }
    }
}
