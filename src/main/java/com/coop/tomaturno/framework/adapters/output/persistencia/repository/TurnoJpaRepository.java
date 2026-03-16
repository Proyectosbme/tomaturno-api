package com.coop.tomaturno.framework.adapters.output.persistencia.repository;

import com.coop.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntityPK;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TurnoJpaRepository implements PanacheRepositoryBase<TurnoJpaEntity, TurnoJpaEntityPK> {

    public TurnoJpaEntity buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return find("idpk.idSucursal = ?1 and idpk.fechaCreacion = ?2 and idpk.codigoTurno = ?3",
                idSucursal, fechaCreacion, codigoTurno).firstResult();
    }

    /** Siguiente número correlativo para codigoTurno (reinicia cada día) */
    public Long obtenerSiguienteNumero(Long idSucursal, LocalDate fecha, String codigoBase) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        long count = count(
                "idpk.idSucursal = ?1 and idpk.fechaCreacion >= ?2 and idpk.fechaCreacion < ?3 and idpk.codigoTurno like ?4",
                idSucursal, inicio, fin, codigoBase + "%");
        return count + 1;
    }

    /** Siguiente id global de referencia */
    public Long obtenerSiguienteId() {
        Long maxId = findAll().stream()
                .mapToLong(t -> t.getId() != null ? t.getId() : 0L)
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    public boolean existeTurnoLlamadoPorPuesto(Long idPuesto, Long idSucursal, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        return count("idPuesto = ?1 and idpk.idSucursal = ?2 and estado = 2 and idpk.fechaCreacion >= ?3 and idpk.fechaCreacion < ?4",
                idPuesto, idSucursal, inicio, fin) > 0;
    }

    public boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        return count("idUsuario = ?1 and idpk.idSucursal = ?2 and estado = 2 and idpk.fechaCreacion >= ?3 and idpk.fechaCreacion < ?4",
                idUsuario, idSucursal, inicio, fin) > 0;
    }

    public List<TurnoJpaEntity> buscarPorFiltros(Long idSucursal, Long idCola, Long idDetalle,
            Integer estado, LocalDate fecha) {
        if (idSucursal == null && idCola == null && estado == null && fecha == null) {
            return List.of();
        }

        StringBuilder query = new StringBuilder("1=1");
        int p = 1;
        List<Object> params = new ArrayList<>();

        if (idSucursal != null) {
            query.append(" and idpk.idSucursal = ?").append(p++);
            params.add(idSucursal);
        }
        if (idCola != null) {
            query.append(" and idCola = ?").append(p++);
            params.add(idCola);
        }
        if (idDetalle != null) {
            query.append(" and idDetalle = ?").append(p++);
            params.add(idDetalle);
        }
        if (estado != null) {
            query.append(" and estado = ?").append(p++);
            params.add(estado);
        }
        if (fecha != null) {
            query.append(" and idpk.fechaCreacion >= ?").append(p++).append(" and idpk.fechaCreacion < ?").append(p++);
            params.add(fecha.atStartOfDay());
            params.add(fecha.plusDays(1).atStartOfDay());
        }

        return list(query.toString(), params.toArray());
    }
}
