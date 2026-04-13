package com.empresa.tomaturno.detallecolaxpuesto.dominio.entity;

import java.time.LocalDateTime;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.exceptions.DetalleColaxPuestoValidationException;
import com.empresa.tomaturno.shared.clases.Auditoria;

public class DetalleColaxPuesto {

    private final Long idPuesto;
    private final Long idSucursalPuesto;
    private final Long idCola;
    private final Long idDetalle;
    private final Long idSucursalCola;
    private final Integer prioridad;
    private Auditoria auditoria;
    // Campos enriquecidos (no persistidos)
    private final String nombreCola;
    private final String nombreDetalle;

    private DetalleColaxPuesto(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle,
            Long idSucursalCola, Integer prioridad, String nombreCola, String nombreDetalle) {
        this.idPuesto = idPuesto;
        this.idSucursalPuesto = idSucursalPuesto;
        this.idCola = idCola;
        this.idDetalle = idDetalle;
        this.idSucursalCola = idSucursalCola;
        this.prioridad = prioridad;
        this.nombreCola = nombreCola;
        this.nombreDetalle = nombreDetalle;
    }

    public static DetalleColaxPuesto inicializar(Long idPuesto, Long idSucursalPuesto, Long idCola,
            Long idDetalle, Long idSucursalCola, Integer prioridad) {
        return new DetalleColaxPuesto(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola,
                prioridad, null, null);
    }

    public static DetalleColaxPuesto reconstituir(Long idPuesto, Long idSucursalPuesto, Long idCola,
            Long idDetalle, Long idSucursalCola, Integer prioridad, Auditoria auditoria) {
        DetalleColaxPuesto d = new DetalleColaxPuesto(idPuesto, idSucursalPuesto, idCola, idDetalle,
                idSucursalCola, prioridad, null, null);
        d.auditoria = auditoria;
        return d;
    }

    public void asignar(String usuario) {
        this.auditoria = Auditoria.deCreacion(usuario, LocalDateTime.now());
        this.validarAsignacion();
    }

    private void validarAsignacion() {
        if (this.idPuesto == null) {
            throw new DetalleColaxPuestoValidationException("El id del puesto es obligatorio");
        }
        if (this.idSucursalPuesto == null) {
            throw new DetalleColaxPuestoValidationException("El id de la sucursal del puesto es obligatorio");
        }
        if (this.idCola == null) {
            throw new DetalleColaxPuestoValidationException("El id de la cola es obligatorio");
        }
        if (this.idDetalle == null) {
            throw new DetalleColaxPuestoValidationException("El id del detalle es obligatorio");
        }
        if (this.idSucursalCola == null) {
            throw new DetalleColaxPuestoValidationException("El id de la sucursal de la cola es obligatorio");
        }
        if (this.prioridad == null || this.prioridad < 1 || this.prioridad > 50) {
            throw new DetalleColaxPuestoValidationException("La prioridad debe ser un número entre 1 y 50");
        }
    }

    public DetalleColaxPuesto conNombres(String nombreCola, String nombreDetalle) {
        DetalleColaxPuesto enriquecido = new DetalleColaxPuesto(this.idPuesto, this.idSucursalPuesto,
                this.idCola, this.idDetalle, this.idSucursalCola, this.prioridad, nombreCola, nombreDetalle);
        enriquecido.auditoria = this.auditoria;
        return enriquecido;
    }

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
}
