package com.empresa.tomaturno.turno.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;
import com.empresa.tomaturno.turno.dominio.vo.EstadoTurno;

public class Turno {

    private Long id;                       // referencia Long auto-generada (para idTurnoRelacionado)
    private Long idSucursal;
    private LocalDateTime fechaCreacion;
    private String codigoTurno;
    private LocalDateTime fechaLlamada;
    private LocalDateTime fechaFinalizacion;
    private Long idCola;
    private Long idDetalle;
    private EstadoTurno estado;
    private Long idTurnoRelacionado;       // id del turno original al reasignar
    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idUsuario;
    private Long idPersona;
    private Integer tipoCasoEspecial;
    // Campo enriquecido (no persistido)
    private String nombreLlamada;

    public Turno() {}

    /* ── Validaciones de estado ───────────────────── */

    public void validarCreacion() {
        if (idSucursal == null) throw new TurnoValidationException("idSucursal es obligatorio");
        if (idCola == null)     throw new TurnoValidationException("idCola es obligatorio");
        if (codigoTurno == null || codigoTurno.isBlank())
            throw new TurnoValidationException("codigoTurno es obligatorio");
        if (estado == null)     throw new TurnoValidationException("estado es obligatorio");
    }

    public void validarTransicionLlamar(Long idPuesto, Long idSucursalPuesto) {
        if (estado != EstadoTurno.CREADO)
            throw new TurnoValidationException("Solo se puede llamar un turno en estado CREADO. Estado actual: " + estado);
        if (idPuesto == null)
            throw new TurnoValidationException("idPuesto es obligatorio para llamar un turno");
        if (idSucursalPuesto == null)
            throw new TurnoValidationException("idSucursalPuesto es obligatorio para llamar un turno");
    }

    public void validarTransicionReasignar() {
        if (estado != EstadoTurno.LLAMADO)
            throw new TurnoValidationException("Solo se puede reasignar un turno en estado LLAMADO. Estado actual: " + estado);
    }

    public void validarTransicionFinalizar() {
        if (estado != EstadoTurno.LLAMADO && estado != EstadoTurno.TRASLADO)
            throw new TurnoValidationException("Solo se puede finalizar un turno en estado LLAMADO o TRASLADO. Estado actual: " + estado);
    }

    /* ── Transiciones de estado ───────────────────── */

    public void llamar(Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        this.idPuesto = idPuesto;
        this.idSucursalPuesto = idSucursalPuesto;
        this.idUsuario = idUsuario;
        this.fechaLlamada = LocalDateTime.now();
        this.estado = EstadoTurno.LLAMADO;
    }

    public void rellamar() {
        this.fechaLlamada = LocalDateTime.now();
    }

    public void marcarTraslado() {
        this.estado = EstadoTurno.TRASLADO;
    }

    /** Re-llama un turno ya finalizado o trasladado (desde historial del operador) */
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

    public void finalizar() {
        this.fechaFinalizacion = LocalDateTime.now();
        this.estado = EstadoTurno.FINALIZADO;
    }

    /* ── Getters / Setters ────────────────────────── */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Long idSucursal) { this.idSucursal = idSucursal; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getCodigoTurno() { return codigoTurno; }
    public void setCodigoTurno(String codigoTurno) { this.codigoTurno = codigoTurno; }
    public LocalDateTime getFechaLlamada() { return fechaLlamada; }
    public void setFechaLlamada(LocalDateTime fechaLlamada) { this.fechaLlamada = fechaLlamada; }
    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }
    public Long getIdCola() { return idCola; }
    public void setIdCola(Long idCola) { this.idCola = idCola; }
    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    public EstadoTurno getEstado() { return estado; }
    public void setEstado(EstadoTurno estado) { this.estado = estado; }
    public Long getIdTurnoRelacionado() { return idTurnoRelacionado; }
    public void setIdTurnoRelacionado(Long idTurnoRelacionado) { this.idTurnoRelacionado = idTurnoRelacionado; }
    public Long getIdPuesto() { return idPuesto; }
    public void setIdPuesto(Long idPuesto) { this.idPuesto = idPuesto; }
    public Long getIdSucursalPuesto() { return idSucursalPuesto; }
    public void setIdSucursalPuesto(Long idSucursalPuesto) { this.idSucursalPuesto = idSucursalPuesto; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getIdPersona() { return idPersona; }
    public void setIdPersona(Long idPersona) { this.idPersona = idPersona; }
    public Integer getTipoCasoEspecial() { return tipoCasoEspecial; }
    public void setTipoCasoEspecial(Integer tipoCasoEspecial) { this.tipoCasoEspecial = tipoCasoEspecial; }
    public String getNombreLlamada() { return nombreLlamada; }
    public void setNombreLlamada(String nombreLlamada) { this.nombreLlamada = nombreLlamada; }
}