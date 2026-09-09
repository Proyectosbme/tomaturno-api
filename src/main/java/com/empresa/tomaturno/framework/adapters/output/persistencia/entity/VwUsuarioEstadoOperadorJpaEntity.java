package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

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
 * Mapea la vista tomaturno.vwusuarioestadooperador (creada por la migración Flyway V8,
 * renombrada junto con toda la tabla/columna/catálogo asociados por la migración V10):
 * un período de estado de operador de HOY por fila (ACTIVA/DESCANSO/CERRADA), ya resuelto
 * con nombre de usuario, puesto, estado y tipo de descanso. Es de solo lectura
 * (respalda una vista, no una tabla), por eso @Immutable.
 */
@Entity
@Immutable
@Table(name = "vwusuarioestadooperador", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VwUsuarioEstadoOperadorJpaEntity {

    @Id
    private Long id;

    private Long idUsuario;
    private Long idSucursal;
    private Long idPuesto;

    private String codigoUsuario;
    private String nombreCompleto;
    private String puesto;

    private Long idEstadoOperador;
    private String estadoOperador;

    private Long idTipoDescanso;
    private String tipoDescanso;
    private String comentario;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
