package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntityPK;

@ApplicationScoped
public class ColaJpaRespository implements PanacheRepositoryBase<ColaJpaEntity, ColaJpaEntityPK> {

    public Long obtenerSiguienteId(Long idSucursal) {
        Long maxId = find("idpk.idSucursal = ?1", idSucursal)
                .stream()
                .mapToLong(c -> c.getIdpk().getId())
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    public ColaJpaEntity buscarPorIdColaYSucursal(Long idCola, Long idSucursal) {
        return find("idpk.id = ?1 and idpk.idSucursal = ?2", idCola, idSucursal).firstResult();
    }

    public boolean existeNombreEnSucursal(Long idSucursal, String nombre) {
        return count("idpk.idSucursal = ?1 and upper(nombre) = upper(?2)", idSucursal, nombre) > 0;
    }

    public List<ColaJpaEntity> buscarPorSucursal(Long idSucursal) {
        return list("idpk.idSucursal = ?1", idSucursal);
    }

    public List<ColaJpaEntity> buscarPorfiltros(Long id, Long idSucursal, String nombre) {
        if (id == null && idSucursal == null && (nombre == null || nombre.isBlank())) {
            return List.of();
        }

        StringBuilder query = new StringBuilder("1=1");
        int paramIndex = 1;
        List<Object> params = new ArrayList<>();

        if (id != null) {
            query.append(" and idpk.id = ?" + paramIndex++);
            params.add(id);
        }
        if (idSucursal != null) {
            query.append(" and idpk.idSucursal = ?" + paramIndex++);
            params.add(idSucursal);
        }
        if (nombre != null && !nombre.isBlank()) {
            query.append(" and upper(nombre) like ?" + paramIndex++);
            params.add("%" + nombre.toUpperCase() + "%");
        }

        return list(query.toString(), params.toArray());
    }
}
