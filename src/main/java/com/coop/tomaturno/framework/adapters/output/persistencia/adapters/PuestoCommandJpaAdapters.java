package com.coop.tomaturno.framework.adapters.output.persistencia.adapters;

import com.coop.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.coop.tomaturno.puesto.dominio.entity.Puesto;
import com.coop.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.coop.tomaturno.framework.adapters.output.mapper.PuestoOutputMapper;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.coop.tomaturno.framework.adapters.output.persistencia.repository.PuestoJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PuestoCommandJpaAdapters implements PuestoCommandRepository {

    private final PuestoJpaRepository puestoJpaRepository;
    private final PuestoOutputMapper puestoOutputMapper;

    public PuestoCommandJpaAdapters(PuestoJpaRepository puestoJpaRepository,
            PuestoOutputMapper puestoOutputMapper) {
        this.puestoJpaRepository = puestoJpaRepository;
        this.puestoOutputMapper = puestoOutputMapper;
    }

    @Override
    public Puesto save(Puesto puesto) {
        Long nextId = puestoJpaRepository.obtenerSiguienteId(puesto.getSucursal().getIdentificador());
        puesto.setIdentificador(nextId);
        PuestoJpaEntity entity = puestoOutputMapper.toJpaEntity(puesto);
        puestoJpaRepository.persist(entity);
        return puestoOutputMapper.toDomain(entity);
    }

    @Override
    public Puesto modificar(Puesto puesto) {
        PuestoJpaEntity existente = puestoJpaRepository
                .buscarPorIdPuestoYSucursal(puesto.getIdentificador(), puesto.getSucursal().getIdentificador());

        if (existente == null) {
            throw new NotFoundException("Puesto no encontrado con idPuesto: " + puesto.getIdentificador()
                    + " e idSucursal: " + puesto.getSucursal().getIdentificador());
        }

        puestoOutputMapper.updateEntityFromDomain(puesto, existente);
        return puestoOutputMapper.toDomain(existente);
    }
}
