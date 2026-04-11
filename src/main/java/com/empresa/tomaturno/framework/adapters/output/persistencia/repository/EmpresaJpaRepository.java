package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.EmpresaJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmpresaJpaRepository implements PanacheRepository<EmpresaJpaEntity> {
}
