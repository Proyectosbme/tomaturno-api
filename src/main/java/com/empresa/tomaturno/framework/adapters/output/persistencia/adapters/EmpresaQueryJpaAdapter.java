package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.empresa.application.query.port.output.EmpresaQueryRepository;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.framework.adapters.output.mapper.EmpresaOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.EmpresaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.EmpresaJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmpresaQueryJpaAdapter implements EmpresaQueryRepository {

    private final EmpresaJpaRepository empresaJpaRepository;
    private final EmpresaOutputMapper empresaOutputMapper;

    public EmpresaQueryJpaAdapter(EmpresaJpaRepository empresaJpaRepository,
                                   EmpresaOutputMapper empresaOutputMapper) {
        this.empresaJpaRepository = empresaJpaRepository;
        this.empresaOutputMapper = empresaOutputMapper;
    }

    @Override
    public Empresa obtener() {
        EmpresaJpaEntity entity = empresaJpaRepository.findById(Empresa.idFijo());
        if (entity == null) {
            return null;
        }
        return empresaOutputMapper.toDomain(entity);
    }
}
