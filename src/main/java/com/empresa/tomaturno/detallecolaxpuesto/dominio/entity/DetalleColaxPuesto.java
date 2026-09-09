package com.empresa.tomaturno.detallecolaxpuesto.dominio.entity;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.exceptions.DetalleColaxPuestoValidationException;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.validador.ValidadorNulosVacios;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.vo.Auditoria;

public final class DetalleColaxPuesto {

    private final Long idPuesto;
    private final Long idSucursalPuesto;
    private final Long idCola;
    private final Long idDetalle;
    private final Long idSucursalCola;
    private Integer prioridad;
    private final Auditoria auditoria;
    // Campos enriquecidos (no persistidos)
    private final String nombreCola;
    private final String nombreDetalle;

    private DetalleColaxPuesto(Builder builder) {
        this.idPuesto = builder.idPuesto;
        this.idSucursalPuesto = builder.idSucursalPuesto;
        this.idCola = builder.idCola;
        this.idDetalle = builder.idDetalle;
        this.idSucursalCola = builder.idSucursalCola;
        this.prioridad = builder.prioridad;
        this.auditoria = builder.auditoria;
        this.nombreCola = builder.nombreCola;
        this.nombreDetalle = builder.nombreDetalle;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /**
     * Único punto de creación/reconstitución: valida el builder antes de construir.
     */
    public static DetalleColaxPuesto of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    public void actualizarPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Copia enriquecida con datos de solo lectura (nombre de la cola y del detalle)
     * que no se persisten. No revalida: los datos de origen ya son válidos.
     */
    public DetalleColaxPuesto conNombres(String nombreCola, String nombreDetalle) {
        return new Builder()
                .idPuesto(this.idPuesto)
                .idSucursalPuesto(this.idSucursalPuesto)
                .idCola(this.idCola)
                .idDetalle(this.idDetalle)
                .idSucursalCola(this.idSucursalCola)
                .prioridad(this.prioridad)
                .auditoria(this.auditoria)
                .nombreCola(nombreCola)
                .nombreDetalle(nombreDetalle)
                .build();
    }

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado
     * inválido.
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.idPuesto, "El id del puesto", DetalleColaxPuestoValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.idSucursalPuesto, "El id de la sucursal del puesto",
                        DetalleColaxPuestoValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.idCola, "El id de la cola", DetalleColaxPuestoValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.idDetalle, "El id del detalle", DetalleColaxPuestoValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.idSucursalCola, "El id de la sucursal de la cola",
                        DetalleColaxPuestoValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.prioridad, "La prioridad", DetalleColaxPuestoValidationException::new)
                .noNulo();
        if (builder.prioridad < 1 || builder.prioridad > 50) {
            throw new DetalleColaxPuestoValidationException("La prioridad debe ser un número entre 1 y 50");
        }
        ValidadorNulosVacios
                .variable(builder.auditoria, "La auditoria de la asignación", DetalleColaxPuestoValidationException::new)
                .noNulo();
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getIdPuesto() {
        return idPuesto;
    }

    public Long getIdSucursalPuesto() {
        return idSucursalPuesto;
    }

    public Long getIdCola() {
        return idCola;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public Long getIdSucursalCola() {
        return idSucursalCola;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public String getNombreCola() {
        return nombreCola;
    }

    public String getNombreDetalle() {
        return nombreDetalle;
    }

    public static class Builder {
        private Long idPuesto;
        private Long idSucursalPuesto;
        private Long idCola;
        private Long idDetalle;
        private Long idSucursalCola;
        private Integer prioridad;
        private Auditoria auditoria;
        private String nombreCola;
        private String nombreDetalle;

        public Builder idPuesto(Long idPuesto) {
            this.idPuesto = idPuesto;
            return this;
        }

        public Builder idSucursalPuesto(Long idSucursalPuesto) {
            this.idSucursalPuesto = idSucursalPuesto;
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

        public Builder idSucursalCola(Long idSucursalCola) {
            this.idSucursalCola = idSucursalCola;
            return this;
        }

        public Builder prioridad(Integer prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Builder auditoria(Auditoria auditoria) {
            this.auditoria = auditoria;
            return this;
        }

        public Builder nombreCola(String nombreCola) {
            this.nombreCola = nombreCola;
            return this;
        }

        public Builder nombreDetalle(String nombreDetalle) {
            this.nombreDetalle = nombreDetalle;
            return this;
        }

        private DetalleColaxPuesto build() {
            return new DetalleColaxPuesto(this);
        }
    }
}
