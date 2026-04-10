package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.framework.adapters.output.mapper.PersonaMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PersonaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.PersonaJpaRepository;
import com.empresa.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.empresa.tomaturno.persona.application.query.port.output.PersonaQueryRepository;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonaJpaAdapter implements PersonaCommandRepository, PersonaQueryRepository {

    private final PersonaJpaRepository personaJpaRepository;
    private final PersonaMapper personaMapper;

    public PersonaJpaAdapter(PersonaJpaRepository personaJpaRepository, PersonaMapper personaMapper) {
        this.personaJpaRepository = personaJpaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    public Persona save(Persona persona) {
        PersonaJpaEntity entity = personaMapper.toEntity(persona);
        personaJpaRepository.persist(entity);
        return personaMapper.toDomain(entity);
    }

    @Override
    public Persona update(Persona persona) {
        PersonaJpaEntity entity = personaJpaRepository.buscarPorDui(persona.getDui());
        if (entity == null) return save(persona);
        personaMapper.updateEntityFromDomain(persona, entity);
        return personaMapper.toDomain(entity);
    }

    @Override
    public Persona buscarPorDui(String dui) {
        PersonaJpaEntity entity = personaJpaRepository.buscarPorDui(dui);
        return entity != null ? personaMapper.toDomain(entity) : null;
    }

    @Override
    public Persona buscarPorId(Long id) {
        PersonaJpaEntity entity = personaJpaRepository.findById(id);
        return entity != null ? personaMapper.toDomain(entity) : null;
    }
}
