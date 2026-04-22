package com.empresa.tomaturno.turno.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class Turno {

    private static final Integer ESTADO_CREADO = 1;
    private static final Integer ESTADO_LLAMADO = 2;
    private static final Integer ESTADO_TRASLADO = 3;
    private static final Integer ESTADO_FINALIZADO = 4;
    private static final Integer ESTADO_SIN_ATENDER = 5;

    private Long id;
    private Long idSucursal;
    private LocalDateTime fechaCreacion;
    private String codigoTurno;
    private LocalDateTime fechaLlamada;
    private LocalDateTime fechaFinalizacion;
    private Long idCola;
    private Long idDetalle;
    private CatalogoDetalle estado;
    private Long idTurnoRelacionado;
    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idUsuario;
    private Long idPersona;
    private Integer tipoCasoEspecial;
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
                .estado(CatalogoDetalle.conCorrelativo(ESTADO_CREADO))
                .idPersona(idPersona)
                .tipoCasoEspecial(tipoCasoEspecial)
                .build();
        turno.validarCreacion();
        return turno;
    }

    public void asignarId(Long id) {
        this.id = id;
    }

    public void llamar(Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        validarTransicionLlamar(idPuesto, idSucursalPuesto);
        this.idPuesto = idPuesto;
        this.idSucursalPuesto = idSucursalPuesto;
        this.idUsuario = idUsuario;
        this.fechaLlamada = LocalDateTime.now();
        this.estado = CatalogoDetalle.conCorrelativo(ESTADO_LLAMADO);
    }

    public void rellamar() {
        this.fechaLlamada = LocalDateTime.now();
    }

    public Turno reasignarA(Long nuevoId, Long idSucursalDestino, Long idColaDestino, Long idDetalleValido) {
        validarTransicionReasignar();
        this.estado = CatalogoDetalle.conCorrelativo(ESTADO_TRASLADO);
        return Turno.builder()
                .id(nuevoId)
                .idSucursal(idSucursalDestino)
                .idCola(idColaDestino)
                .idDetalle(idDetalleValido)
                .codigoTurno(this.codigoTurno)
                .fechaCreacion(LocalDateTime.now())
                .estado(CatalogoDetalle.conCorrelativo(ESTADO_CREADO))
                .idTurnoRelacionado(this.id)
                .build();
    }

    public void rellamarDesdeHistorial(Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        Integer correlativo = estado != null ? estado.getCorrelativo() : null;
        if (!ESTADO_FINALIZADO.equals(correlativo) && !ESTADO_TRASLADO.equals(correlativo))
            throw new TurnoValidationException(
                    "Solo se puede re-llamar un turno en estado FINALIZADO o TRASLADO. Estado actual: " + correlativo);
        this.idPuesto = idPuesto;
        this.idSucursalPuesto = idSucursalPuesto;
        this.idUsuario = idUsuario;
        this.fechaLlamada = LocalDateTime.now();
        this.estado = CatalogoDetalle.conCorrelativo(ESTADO_LLAMADO);
    }

    public void sinAtender() {
        Integer correlativo = estado != null ? estado.getCorrelativo() : null;
        if (!ESTADO_LLAMADO.equals(correlativo))
            throw new TurnoValidationException(
                    "Solo se puede marcar sin atender un turno en estado LLAMADO. Estado actual: " + correlativo);
        this.estado = CatalogoDetalle.conCorrelativo(ESTADO_SIN_ATENDER);
    }

    public void finalizar() {
        validarTransicionFinalizar();
        this.fechaFinalizacion = LocalDateTime.now();
        this.estado = CatalogoDetalle.conCorrelativo(ESTADO_FINALIZADO);
    }

    public void enriquecerNombreLlamada(String nombreLlamada) {
        this.nombreLlamada = nombreLlamada;
    }

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
        Integer correlativo = estado != null ? estado.getCorrelativo() : null;
        if (!ESTADO_CREADO.equals(correlativo) && !ESTADO_SIN_ATENDER.equals(correlativo))
            throw new TurnoValidationException(
                    "Solo se puede llamar un turno en estado CREADO. Estado actual: " + correlativo);
        if (idPuesto == null)
            throw new TurnoValidationException("idPuesto es obligatorio para llamar un turno");
        if (idSucursalPuesto == null)
            throw new TurnoValidationException("idSucursalPuesto es obligatorio para llamar un turno");
    }

    private void validarTransicionReasignar() {
        Integer correlativo = estado != null ? estado.getCorrelativo() : null;
        if (!ESTADO_LLAMADO.equals(correlativo))
            throw new TurnoValidationException(
                    "Solo se puede reasignar un turno en estado LLAMADO. Estado actual: " + correlativo);
    }

    private void validarTransicionFinalizar() {
        Integer correlativo = estado != null ? estado.getCorrelativo() : null;
        if (!ESTADO_LLAMADO.equals(correlativo) && !ESTADO_TRASLADO.equals(correlativo))
            throw new TurnoValidationException(
                    "Solo se puede finalizar un turno en estado LLAMADO o TRASLADO. Estado actual: " + correlativo);
    }

    /* ── Getters ─────────────────────────────────── */

    public Long getId() { return id; }
    public Long getIdSucursal() { return idSucursal; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getCodigoTurno() { return codigoTurno; }
    public LocalDateTime getFechaLlamada() { return fechaLlamada; }
    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public Long getIdCola() { return idCola; }
    public Long getIdDetalle() { return idDetalle; }
    public CatalogoDetalle getEstado() { return estado; }
    public Long getIdTurnoRelacionado() { return idTurnoRelacionado; }
    public Long getIdPuesto() { return idPuesto; }
    public Long getIdSucursalPuesto() { return idSucursalPuesto; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdPersona() { return idPersona; }
    public Integer getTipoCasoEspecial() { return tipoCasoEspecial; }
    public String getNombreLlamada() { return nombreLlamada; }

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
        private CatalogoDetalle estado;
        private Long idTurnoRelacionado;
        private Long idPuesto;
        private Long idSucursalPuesto;
        private Long idUsuario;
        private Long idPersona;
        private Integer tipoCasoEspecial;

        private Builder() {}

        public Builder id(Long id) { this.id = id; return this; }
        public Builder idSucursal(Long idSucursal) { this.idSucursal = idSucursal; return this; }
        public Builder fechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; return this; }
        public Builder codigoTurno(String codigoTurno) { this.codigoTurno = codigoTurno; return this; }
        public Builder fechaLlamada(LocalDateTime fechaLlamada) { this.fechaLlamada = fechaLlamada; return this; }
        public Builder fechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; return this; }
        public Builder idCola(Long idCola) { this.idCola = idCola; return this; }
        public Builder idDetalle(Long idDetalle) { this.idDetalle = idDetalle; return this; }
        public Builder estado(CatalogoDetalle estado) { this.estado = estado; return this; }
        public Builder idTurnoRelacionado(Long idTurnoRelacionado) { this.idTurnoRelacionado = idTurnoRelacionado; return this; }
        public Builder idPuesto(Long idPuesto) { this.idPuesto = idPuesto; return this; }
        public Builder idSucursalPuesto(Long idSucursalPuesto) { this.idSucursalPuesto = idSucursalPuesto; return this; }
        public Builder idUsuario(Long idUsuario) { this.idUsuario = idUsuario; return this; }
        public Builder idPersona(Long idPersona) { this.idPersona = idPersona; return this; }
        public Builder tipoCasoEspecial(Integer tipoCasoEspecial) { this.tipoCasoEspecial = tipoCasoEspecial; return this; }

        public Turno build() { return new Turno(this); }
    }
}
