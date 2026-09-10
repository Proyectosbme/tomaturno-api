package com.empresa.tomaturno.cola.dominio.entity;

import java.util.List;

import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.cola.dominio.validador.ValidadorNulosVacios;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;
import com.empresa.tomaturno.cola.dominio.vo.Sucursal;

public final class Cola {

    private final Long identificador;
    private String nombre;
    private String codigo;
    private Estado estado;
    private final Sucursal sucursal;
    private Auditoria auditoriaCreacion;
    private Auditoria auditoriaModificacion;
    private final List<Detalle> detalles;

    private Cola(Builder builder) {
        this.identificador = builder.identificador;
        this.nombre = builder.nombre != null ? builder.nombre.trim().toUpperCase() : null;
        this.codigo = builder.codigo != null ? builder.codigo.trim().toUpperCase() : null;
        this.estado = builder.estado;
        this.sucursal = builder.sucursal;
        this.auditoriaCreacion = builder.auditoriaCreacion;
        this.auditoriaModificacion = builder.auditoriaModificacion;
        this.detalles = builder.detalles;
    }

    // ─── Builder ──────────────────────────────────────────────────────────

    /**
     * Único punto de creación/reconstitución: valida el builder antes de construir.
     */
    public static Cola of(Builder builder) {
        validarCreacion(builder);
        return builder.build();
    }

    // ─── Comportamiento ───────────────────────────────────────────────────

    /**
     * auditoriaModificacion ya viene construida (Auditoria.of(usuario, fecha));
     * esta entidad no la arma.
     */
    public void modificar(String nombre, String codigo, Estado estado, Auditoria auditoriaModificacion) {
        if (nombre != null) {
            this.nombre = nombre.trim().toUpperCase();
        }
        if (codigo != null) {
            this.codigo = codigo;
        }
        if (estado != null) {
            this.estado = estado;
        }
        aplicarAuditoriaModificacion(auditoriaModificacion);
    }

    private void aplicarAuditoriaModificacion(Auditoria auditoriaModificacion) {
        ValidadorNulosVacios
                .variable(auditoriaModificacion, "La auditoria de modificacion de la cola",
                        ColaValidationException::new)
                .noNulo();
        this.auditoriaModificacion = auditoriaModificacion;
    }

    public void agregarDetalle(Detalle detalle) {
        this.detalles.add(detalle);
    }

    /**
     * Detalle.of es protected: solo Cola construye un Detalle nuevo. La
     * auditoría viene ya armada de afuera.
     */
    public Detalle crearDetalle(Detalle.Builder builder, Auditoria auditoriaCreacion) {
        builder.auditoriaCreacion(auditoriaCreacion);
        // El código del detalle son dos letras: la de esta cola más la propia que trae
        // el builder.
        builder.codigo(this.codigo + (builder.getCodigo() != null ? builder.getCodigo() : ""));
        Detalle detalle = Detalle.of(builder);
        validarNombreDetalleUnico(detalle.getNombre());
        validarCodigoDetalleUnico(detalle.getCodigo());
        return detalle;
    }

    /** Reconstitución desde persistencia: los datos ya son válidos, no recompone código ni valida unicidad. */
    public static Detalle reconstituirDetalle(Detalle.Builder builder) {
        return Detalle.of(builder);
    }

    public void validarCodigoDetalleUnico(String codigoDetalle) {
        boolean existe = detalles != null && detalles.stream()
                .anyMatch(d -> d.getCodigo().equalsIgnoreCase(codigoDetalle));
        if (existe) {
            throw new ColaValidationException(
                    "Ya existe un detalle con el codigo '" + codigoDetalle + "' en esta cola");
        }
    }

    /**
     * Detalle.modificar es protected: los casos de uso pasan por aquí en vez de
     * llamarlo directo.
     */
    public Detalle modificarDetalle(Long correlativo, String nombre, String codigo, Estado estado,
            Auditoria auditoriaModificacion) {
        Detalle detalle = detalles.stream()
                .filter(d -> d.getCorrelativo().equals(correlativo))
                .findFirst()
                .orElseThrow(() -> new ColaValidationException("Detalle no encontrado en la cola"));
        detalle.modificar(nombre, codigo, estado, auditoriaModificacion);
        return detalle;
    }

    /**
     * La lista de detalles ya vive en el agregado; se recorre aquí en vez de
     * sacarla a una especificación aparte.
     */
    public void validarNombreDetalleUnico(String nombreDetalle) {
        if (existeDetalleConNombre(nombreDetalle)) {
            throw new ColaValidationException(
                    "Ya existe un detalle con el nombre '" + nombreDetalle + "' en esta cola");
        }
    }

    public boolean existeDetalleConNombre(String nombreDetalle) {
        return detalles != null && detalles.stream()
                .anyMatch(d -> d.getNombre().equalsIgnoreCase(nombreDetalle));
    }

    /**
     * El código del turno usa el del detalle si el turno pertenece a uno; si no, el
     * de la cola.
     */
    public String resolverCodigoBase(Detalle detalle) {
        return detalle != null ? detalle.getCodigo() : this.codigo;
    }

    /**
     * Si la cola no tiene detalles no hay que elegir uno; si tiene, es obligatorio
     * y debe existir.
     */
    public Long resolverDetalleReasignacion(Long idDetalleDestino) {
        if (detalles == null || detalles.isEmpty()) {
            return null;
        }
        ValidadorNulosVacios.variable(idDetalleDestino, "El detalle destino", ColaValidationException::new)
                .noNulo();
        detalles.stream()
                .filter(d -> d.getCorrelativo().equals(idDetalleDestino))
                .findFirst()
                .orElseThrow(() -> new ColaValidationException("Detalle destino no encontrado en la cola"));
        return idDetalleDestino;
    }

    /**
     * Valida el builder antes de construir: el objeto nunca existe en un estado
     * inválido.
     */
    private static void validarCreacion(Builder builder) {
        ValidadorNulosVacios.variable(builder.nombre, "El nombre de la cola", ColaValidationException::new)
                .noNuloNoVacio();
        ValidadorNulosVacios.variable(builder.codigo, "El codigo de la cola", ColaValidationException::new)
                .noNuloNoVacio();
        if (!builder.codigo.trim().matches("[A-Za-z]")) {
            throw new ColaValidationException("El codigo de la cola debe ser una sola letra");
        }
        ValidadorNulosVacios.variable(builder.estado, "El estado de la cola", ColaValidationException::new)
                .noNulo();
        ValidadorNulosVacios.variable(builder.sucursal, "La sucursal de la cola", ColaValidationException::new)
                .noNulo();
        ValidadorNulosVacios
                .variable(builder.auditoriaCreacion, "La auditoria de creacion de la cola",
                        ColaValidationException::new)
                .noNulo();
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Long getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public Auditoria getAuditoriaCreacion() {
        return auditoriaCreacion;
    }

    public Auditoria getAuditoriaModificacion() {
        return auditoriaModificacion;
    }

    public List<Detalle> getDetalles() {
        return detalles == null ? List.of() : List.copyOf(detalles);
    }

    public static class Builder {
        private Long identificador;
        private String nombre;
        private String codigo;
        private Estado estado;
        private Sucursal sucursal;
        private Auditoria auditoriaCreacion;
        private Auditoria auditoriaModificacion;
        private List<Detalle> detalles;

        public Builder identificador(Long identificador) {
            this.identificador = identificador;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder estado(Estado estado) {
            this.estado = estado;
            return this;
        }

        public Builder sucursal(Sucursal sucursal) {
            this.sucursal = sucursal;
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

        public Builder detalles(List<Detalle> detalles) {
            this.detalles = detalles;
            return this;
        }

        private Cola build() {
            return new Cola(this);
        }
    }
}
