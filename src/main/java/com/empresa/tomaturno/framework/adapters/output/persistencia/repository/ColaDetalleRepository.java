package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaPK;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ColaDetalleRepository implements PanacheRepositoryBase<DetalleColaJpaEntity, DetalleColaPK> {

    public Long obtenerSiguienteIdDetalle(Long idCola, Integer idSucursal) {
        Long maxId = find("id.idCola = ?1 and id.idSucursal = ?2", idCola, idSucursal)
                .stream()
                .mapToLong(d -> d.getId().getIdDetalle())
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    public DetalleColaJpaEntity buscarPorId(Long idCola, Integer idSucursal, Long idDetalle) {
        return find("id.idCola = ?1 and id.idSucursal = ?2 and id.idDetalle = ?3",
                idCola, idSucursal, idDetalle).firstResult();
    }

    public boolean existeNombreEnCola(Long idCola, Long idSucursal, String nombre) {
        return count("id.idCola = ?1 and id.idSucursal = ?2 and upper(nombre) = upper(?3)",
                idCola, idSucursal.intValue(), nombre) > 0;
    }

    public List<DetalleColaJpaEntity> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle) {
        if (idSucursal == null) return null;
        if (idCola == null && idDetalle == null) return null;

        StringBuilder query = new StringBuilder("id.idSucursal = ?1");
        List<Object> params = new ArrayList<>();
        params.add(idSucursal.intValue());

        if (idCola != null) {
            params.add(idCola);
            query.append(" and id.idCola = ?").append(params.size());
        }
        if (idDetalle != null) {
            params.add(idDetalle);
            query.append(" and id.idDetalle = ?").append(params.size());
        }

        return find(query.toString(), params.toArray()).list();
    }
}
