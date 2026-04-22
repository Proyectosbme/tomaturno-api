package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntityPK;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatalogoDetalleJpaRepository implements PanacheRepositoryBase<CatalogoDetalleJpaEntity, CatalogoDetalleJpaEntityPK> {

    public List<CatalogoDetalleJpaEntity> buscarPorIdCatalogo(Long idCatalogo) {
        return list("id.idCatalogo", idCatalogo);
    }

    public Long obtenerSiguienteCorrelativo(Long idCatalogo) {
        Long max = find("id.idCatalogo = ?1 ORDER BY id.idDetalle DESC", idCatalogo)
                .firstResultOptional()
                .map(d -> d.getId().getIdDetalle())
                .orElse(0L);
        return max + 1;
    }
}
