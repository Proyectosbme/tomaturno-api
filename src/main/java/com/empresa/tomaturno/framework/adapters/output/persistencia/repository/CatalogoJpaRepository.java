package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoJpaEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatalogoJpaRepository implements PanacheRepository<CatalogoJpaEntity> {

    public Long obtenerSiguienteCorrelativo() {
        Long max = find("ORDER BY id DESC").firstResultOptional()
                .map(CatalogoJpaEntity::getId)
                .orElse(0L);
        return max + 1;
    }
}
