package com.coop.tomaturno.framework.adapters.output.persistencia.repository;

import com.coop.tomaturno.framework.adapters.output.persistencia.entity.PersonaJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonaJpaRepository implements PanacheRepositoryBase<PersonaJpaEntity, Long> {

    public PersonaJpaEntity buscarPorDui(String dui) {
        return find("dui", dui).firstResult();
    }
}
