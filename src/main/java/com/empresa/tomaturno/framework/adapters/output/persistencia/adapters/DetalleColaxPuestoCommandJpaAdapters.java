package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.framework.adapters.output.mapper.DetalleColaxPuestoOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaxPuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.DetalleColaxPuestoJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DetalleColaxPuestoCommandJpaAdapters implements DetalleColaxPuestoCommandRepository {

    private final DetalleColaxPuestoJpaRepository repository;
    private final DetalleColaxPuestoOutputMapper mapper;

    public DetalleColaxPuestoCommandJpaAdapters(DetalleColaxPuestoJpaRepository repository,
                                                 DetalleColaxPuestoOutputMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DetalleColaxPuesto save(DetalleColaxPuesto asignacion) {
        DetalleColaxPuestoJpaEntity entity = mapper.toJpaEntity(asignacion);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    @Transactional
    public void eliminar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola) {
        repository.eliminarAsignacion(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
    }
}
