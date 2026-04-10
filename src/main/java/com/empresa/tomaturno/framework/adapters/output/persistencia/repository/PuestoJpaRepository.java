package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntityPK;

@ApplicationScoped
public class PuestoJpaRepository implements PanacheRepositoryBase<PuestoJpaEntity, PuestoJpaEntityPK> {

    public Long obtenerSiguienteId(Long idSucursal) {
        Long maxId = find("idpk.idSucursal = ?1", idSucursal)
                .stream()
                .mapToLong(p -> p.getIdpk().getId())
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    public PuestoJpaEntity buscarPorIdPuestoYSucursal(Long idPuesto, Long idSucursal) {
        return find("idpk.id = ?1 and idpk.idSucursal = ?2", idPuesto, idSucursal).firstResult();
    }

    public String obtenerNombreLlamada(Long idPuesto, Long idSucursal) {
        PuestoJpaEntity puesto = buscarPorIdPuestoYSucursal(idPuesto, idSucursal);
        if (puesto == null) return null;
        return (puesto.getNombreLlamada() != null && !puesto.getNombreLlamada().isBlank())
                ? puesto.getNombreLlamada()
                : puesto.getNombre();
    }

    public boolean existeNombreEnSucursal(Long idSucursal, String nombre) {
        return count("idpk.idSucursal = ?1 and upper(nombre) = upper(?2)", idSucursal, nombre) > 0;
    }

    public List<PuestoJpaEntity> buscarPorFiltros(Long idSucursal, String nombre) {
        if (idSucursal == null && (nombre == null || nombre.isBlank())) {
            return List.of();
        }

        StringBuilder query = new StringBuilder("1=1");
        int paramIndex = 1;
        List<Object> params = new ArrayList<>();

        if (idSucursal != null) {
            query.append(" and idpk.idSucursal = ?").append(paramIndex++);
            params.add(idSucursal);
        }
        if (nombre != null && !nombre.isBlank()) {
            query.append(" and upper(nombre) like ?").append(paramIndex++);
            params.add("%" + nombre.toUpperCase() + "%");
        }

        return list(query.toString(), params.toArray());
    }
}
