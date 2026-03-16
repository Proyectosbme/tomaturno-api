package com.coop.tomaturno.configuracion.dominio.entity;

import com.coop.tomaturno.configuracion.dominio.exceptions.ConfiguracionValidationException;
import com.coop.tomaturno.configuracion.dominio.vo.Auditoria;
import com.coop.tomaturno.configuracion.dominio.vo.Estado;

public class Configuracion {

    private Long idConfiguracion;
    private Long idSucursal;
    /** Clave de la configuración (ej: VALIDAR_IP, REINICIAR_DIARIO, CORRELATIVO_ACTUAL) */
    private String nombre;
    /** Valor numérico: 0/1 para booleanos, número correlativo para secuencias */
    private Integer parametro;
    /** Valor de texto: prefijos, formatos (ej: "C-", "P-") */
    private String valorTexto;
    /** Descripción legible del parámetro */
    private String descripcion;
    private Estado estado;
    private Auditoria auditoria;

    // Campo enriquecido (no persistido)
    private String nombreSucursal;

    public Configuracion() { }

    public void modificar(String nombre, Integer parametro, String valorTexto,
                          String descripcion, Estado estado) {
        this.nombre = nombre;
        this.parametro = parametro;
        this.valorTexto = valorTexto;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public void auditoriaCreacion(String usuario, java.time.LocalDateTime fecha) {
        if (this.auditoria == null) this.auditoria = new Auditoria();
        this.auditoria.creacion(usuario, fecha);
    }

    public void auditoriaModificacion(String usuario, java.time.LocalDateTime fecha) {
        if (this.auditoria == null) {
            throw new ConfiguracionValidationException("No tiene auditoría de creación");
        }
        this.auditoria.modificacion(usuario, fecha);
    }

    public void validarCreacion() {
        if (this.nombre == null || this.nombre.isBlank()) {
            throw new ConfiguracionValidationException("El nombre de la configuración es obligatorio");
        }
        if (this.idSucursal == null) {
            throw new ConfiguracionValidationException("La sucursal es obligatoria");
        }
        if (this.estado == null) {
            throw new ConfiguracionValidationException("El estado es obligatorio");
        }
        auditoria.validarCreacion();
    }

    public void validarModificacion() {
        if (this.idConfiguracion == null) {
            throw new ConfiguracionValidationException("El identificador de la configuración es obligatorio");
        }
        if (this.nombre == null || this.nombre.isBlank()) {
            throw new ConfiguracionValidationException("El nombre de la configuración es obligatorio");
        }
        if (this.estado == null) {
            throw new ConfiguracionValidationException("El estado es obligatorio");
        }
        auditoria.validarModificacion();
    }

    public Long getIdConfiguracion() { return idConfiguracion; }
    public void setIdConfiguracion(Long idConfiguracion) { this.idConfiguracion = idConfiguracion; }
    public Long getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Long idSucursal) { this.idSucursal = idSucursal; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getParametro() { return parametro; }
    public void setParametro(Integer parametro) { this.parametro = parametro; }
    public String getValorTexto() { return valorTexto; }
    public void setValorTexto(String valorTexto) { this.valorTexto = valorTexto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Auditoria getAuditoria() { return auditoria; }
    public void setAuditoria(Auditoria auditoria) { this.auditoria = auditoria; }
    public String getNombreSucursal() { return nombreSucursal; }
    public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }
}
