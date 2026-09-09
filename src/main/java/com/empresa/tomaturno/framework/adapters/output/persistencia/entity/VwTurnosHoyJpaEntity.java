package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

/**
 * Mapea la vista tomaturno.vwturnoshoy (ver V2__init_data.sql / V4__correccion_vistas_turno.sql):
 * un turno por fila, ya resuelto con nombres de sucursal/usuario/puesto/cola/detalle/estado,
 * filtrado a los turnos creados hoy. Es de solo lectura (respaldada por una vista, no una tabla),
 * por eso @Immutable — Hibernate nunca debe intentar hacer UPDATE/INSERT/DELETE sobre esta entidad.
 */
@Entity
@Immutable
@Table(name = "vwturnoshoy", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VwTurnosHoyJpaEntity {

    @Id
    private Long id;

    private String codigoTurno;

    private Long idSucursalTicket;
    private String sucursalTicket;

    private Long idUsuario;
    private String nombreCompleto;
    private String codigoUsuario;

    private Long idPuesto;
    private Long idPuestoSucursal;
    private String puesto;

    private Long idCola;
    private String cola;

    // La vista nombra esta columna "idetalle" (sin la segunda "d"), no "iddetalle".
    @Column(name = "idetalle")
    private Long idDetalle;
    private String detalle;

    private Integer tipoCasoEspecial;

    @Column(name = "caso_especial")
    private String casoEspecial;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLlamada;
    private LocalDateTime fechaFinalizacion;

    private Long idCatalogoEstado;
    private Long idCatalogoEstadoDetalle;

    // Nombre del estado del turno (CREADO, LLAMADO, FINALIZADO, ...), no confundir con el
    // patrón usual de "estado" Integer activo/inactivo que usan las demás entidades.
    @Column(name = "estado")
    private String estadoTurno;

    private Long idTurnoRelacionado;
}
