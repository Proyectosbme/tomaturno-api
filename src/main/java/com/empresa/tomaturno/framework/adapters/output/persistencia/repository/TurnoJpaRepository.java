package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntityPK;

@ApplicationScoped
public class TurnoJpaRepository implements PanacheRepositoryBase<TurnoJpaEntity, TurnoJpaEntityPK> {

    public TurnoJpaEntity buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return find("idpk.idSucursal = ?1 and idpk.fechaCreacion = ?2 and idpk.codigoTurno = ?3",
                idSucursal, fechaCreacion, codigoTurno).firstResult();
    }

    /** Siguiente número correlativo para codigoTurno (reinicia cada día) */
    public Long obtenerSiguienteNumero(Long idSucursal, LocalDate fecha, String codigoBase) {
        // Lock por sucursal + cola para evitar correlativos duplicados en peticiones
        // simultáneas
        long lockKey = idSucursal * 100_000L + Math.abs((long) codigoBase.hashCode() % 100_000);
        getEntityManager()
                .createNativeQuery("SELECT pg_advisory_xact_lock(:key)")
                .setParameter("key", lockKey)
                .getSingleResult();

        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        long count = count(
                "idpk.idSucursal = ?1 and idpk.fechaCreacion >= ?2 and idpk.fechaCreacion < ?3 and idpk.codigoTurno like ?4",
                idSucursal, inicio, fin, codigoBase + "%");
        return count + 1;
    }

    /** Siguiente id global de referencia, vía secuencia de Postgres (atómico) */
    public Long obtenerSiguienteId() {
        Number siguiente = (Number) getEntityManager()
                .createNativeQuery("SELECT nextval('tomaturno.seq_turno_id')")
                .getSingleResult();
        return siguiente.longValue();
    }

    public boolean existeTurnoLlamadoPorPuesto(Long idPuesto, Long idSucursal, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        return count(
                "idPuesto = ?1 and idpk.idSucursal = ?2 and idCatalogoEstadoDetalle = 2 and idpk.fechaCreacion >= ?3 and idpk.fechaCreacion < ?4",
                idPuesto, idSucursal, inicio, fin) > 0;
    }

    public boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        return count(
                "idUsuario = ?1 and idpk.idSucursal = ?2 and idCatalogoEstadoDetalle = 2 and idpk.fechaCreacion >= ?3 and idpk.fechaCreacion < ?4",
                idUsuario, idSucursal, inicio, fin) > 0;
    }

    /** MAX(fechaLlamada) histórico del usuario en la sucursal (todos los turnos, sin filtrar por fecha ni estado) */
    public LocalDateTime obtenerUltimaFechaLlamadaPorUsuario(Long idUsuario, Long idSucursal) {
        return getEntityManager()
                .createQuery(
                        "select max(t.fechaLlamada) from TurnoJpaEntity t where t.idUsuario = ?1 and t.idpk.idSucursal = ?2",
                        LocalDateTime.class)
                .setParameter(1, idUsuario)
                .setParameter(2, idSucursal)
                .getSingleResult();
    }

    public List<TurnoJpaEntity> buscarPorFiltros(Long idSucursal, Long idCola, Long idDetalle,
            Integer estado, LocalDate fecha, Long idPuesto, Long idSucursalPuesto) {
        if (idSucursal == null && idCola == null && estado == null && fecha == null) {
            return List.of();
        }

        boolean conPrioridad = idPuesto != null && idSucursalPuesto != null;

        StringBuilder jpql = new StringBuilder("SELECT t FROM TurnoJpaEntity t ");
        if (conPrioridad) {
            // INNER JOIN a propósito: si se filtra por puesto, solo interesan turnos de
            // cola+detalle que ese puesto tiene asignados en detallecolaxpuesto. Con LEFT JOIN
            // los turnos de colas/detalles NO asignados igual se colaban en el resultado
            // (con prioridad 9999 por el COALESCE), permitiendo que "llamar siguiente" tomara
            // un turno de un detalle que ese operador no atiende.
            jpql.append("JOIN DetalleColaxPuestoJpaEntity d ")
                    .append("ON t.idCola = d.id.idCola ")
                    .append("AND t.idDetalle = d.id.idDetalle ")
                    .append("AND t.idpk.idSucursal = d.id.idSucursalCola ")
                    .append("AND d.id.idPuesto = :idPuesto ")
                    .append("AND d.id.idSucursalPuesto = :idSucursalPuesto ");
        }
        jpql.append("WHERE 1=1 ");

        if (idSucursal != null)
            jpql.append("AND t.idpk.idSucursal = :idSucursal ");
        if (idCola != null)
            jpql.append("AND t.idCola = :idCola ");
        if (idDetalle != null)
            jpql.append("AND t.idDetalle = :idDetalle ");
        if (estado != null)
            jpql.append("AND t.idCatalogoEstadoDetalle = :estado ");
        if (fecha != null)
            jpql.append("AND t.idpk.fechaCreacion >= :fechaInicio AND t.idpk.fechaCreacion < :fechaFin ");

        if (conPrioridad) {
            jpql.append("ORDER BY COALESCE(d.prioridad, 9999) ASC, t.idpk.fechaCreacion ASC");
        } else {
            jpql.append("ORDER BY t.idpk.fechaCreacion ASC");
        }

        TypedQuery<TurnoJpaEntity> query = getEntityManager().createQuery(jpql.toString(), TurnoJpaEntity.class);

        if (conPrioridad) {
            query.setParameter("idPuesto", idPuesto);
            query.setParameter("idSucursalPuesto", idSucursalPuesto);
        }
        if (idSucursal != null)
            query.setParameter("idSucursal", idSucursal);
        if (idCola != null)
            query.setParameter("idCola", idCola);
        if (idDetalle != null)
            query.setParameter("idDetalle", idDetalle);
        if (estado != null)
            query.setParameter("estado", estado.longValue());
        if (fecha != null) {
            query.setParameter("fechaInicio", fecha.atStartOfDay());
            query.setParameter("fechaFin", fecha.plusDays(1).atStartOfDay());
        }

        return query.getResultList();
    }

    public boolean existeTurno(Long idSucursal,
            Integer estado, LocalDate fecha, Long idPuesto, Long idSucursalPuesto) {
        if (idSucursal == null && estado == null && fecha == null) {
            return false;
        }

        StringBuilder jpql = new StringBuilder("SELECT COUNT(t) FROM TurnoJpaEntity t WHERE 1=1 ");

        if (idSucursal != null)
            jpql.append("AND t.idpk.idSucursal = :idSucursal ");
        if (estado != null)
            jpql.append("AND t.idCatalogoEstadoDetalle = :estado ");
        if (fecha != null)
            jpql.append("AND t.idpk.fechaCreacion >= :fechaInicio AND t.idpk.fechaCreacion < :fechaFin ");
        if (idPuesto != null)
            jpql.append("AND t.idPuesto = :idPuesto ");
        if (idSucursalPuesto != null)
            jpql.append("AND t.idSucursalPuesto = :idSucursalPuesto ");

        TypedQuery<Long> query = getEntityManager().createQuery(jpql.toString(), Long.class);

        if (idSucursal != null)
            query.setParameter("idSucursal", idSucursal);
        if (estado != null)
            query.setParameter("estado", estado.longValue());
        if (fecha != null) {
            query.setParameter("fechaInicio", fecha.atStartOfDay());
            query.setParameter("fechaFin", fecha.plusDays(1).atStartOfDay());
        }
        if (idPuesto != null)
            query.setParameter("idPuesto", idPuesto);
        if (idSucursalPuesto != null)
            query.setParameter("idSucursalPuesto", idSucursalPuesto);

        return query.getSingleResult() > 0;
    }
}
