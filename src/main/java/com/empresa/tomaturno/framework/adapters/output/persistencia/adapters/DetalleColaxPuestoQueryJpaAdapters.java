package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.framework.adapters.output.mapper.DetalleColaxPuestoOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaxPuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ColaDetalleRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ColaJpaRespository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.DetalleColaxPuestoJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DetalleColaxPuestoQueryJpaAdapters implements DetalleColaxPuestoQueryRepository {

    private final DetalleColaxPuestoJpaRepository repository;
    private final DetalleColaxPuestoOutputMapper mapper;
    private final ColaJpaRespository colaJpaRespository;
    private final ColaDetalleRepository colaDetalleRepository;

    public DetalleColaxPuestoQueryJpaAdapters(DetalleColaxPuestoJpaRepository repository,
                                               DetalleColaxPuestoOutputMapper mapper,
                                               ColaJpaRespository colaJpaRespository,
                                               ColaDetalleRepository colaDetalleRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.colaJpaRespository = colaJpaRespository;
        this.colaDetalleRepository = colaDetalleRepository;
    }

    @Override
    public List<DetalleColaxPuesto> buscarPorPuesto(Long idPuesto, Long idSucursalPuesto) {
        List<DetalleColaxPuestoJpaEntity> entities = repository.buscarPorPuesto(idPuesto, idSucursalPuesto);
        if (entities.isEmpty()) return List.of();

        List<DetalleColaxPuesto> asignaciones = entities.stream()
                .map(mapper::toDomain)
                .toList();

        return enriquecerConNombres(asignaciones);
    }

    @Override
    public boolean existeAsignacion(Long idPuesto, Long idSucursalPuesto,
                                     Long idCola, Long idDetalle, Long idSucursalCola) {
        return repository.existeAsignacion(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
    }

    private List<DetalleColaxPuesto> enriquecerConNombres(List<DetalleColaxPuesto> asignaciones) {
        List<Long> idsColas = asignaciones.stream()
                .map(DetalleColaxPuesto::getIdCola).distinct().toList();

        Map<Long, String> nombresColas = colaJpaRespository
                .list("idpk.id in ?1", idsColas).stream()
                .collect(Collectors.toMap(
                        c -> c.getIdpk().getId(),
                        ColaJpaEntity::getNombre,
                        (a, b) -> a));

        return asignaciones.stream()
                .map(a -> {
                    String nombreCola = nombresColas.getOrDefault(a.getIdCola(), "");
                    DetalleColaJpaEntity detalle = colaDetalleRepository.buscarPorId(
                            a.getIdCola(), a.getIdSucursalCola().intValue(), a.getIdDetalle());
                    String nombreDetalle = detalle != null ? detalle.getNombre() : null;
                    return a.conNombres(nombreCola, nombreDetalle);
                })
                .toList();
    }
}
