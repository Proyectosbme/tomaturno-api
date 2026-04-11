package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaCommandRepository;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.framework.adapters.exceptions.NotFoundException;
import com.empresa.tomaturno.framework.adapters.output.mapper.EmpresaOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.EmpresaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.EmpresaJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmpresaCommandJpaAdapter implements EmpresaCommandRepository {

    private final EmpresaJpaRepository empresaJpaRepository;
    private final EmpresaOutputMapper empresaOutputMapper;

    public EmpresaCommandJpaAdapter(EmpresaJpaRepository empresaJpaRepository,
                                     EmpresaOutputMapper empresaOutputMapper) {
        this.empresaJpaRepository = empresaJpaRepository;
        this.empresaOutputMapper = empresaOutputMapper;
    }

    @Override
    public Empresa actualizar(Empresa empresa) {
        EmpresaJpaEntity existente = empresaJpaRepository.findById(empresa.getId());
        if (existente == null) {
            throw new NotFoundException("No se encontró la empresa con id: " + empresa.getId());
        }
        empresaOutputMapper.updateEntityFromDomain(empresa, existente);
        empresaJpaRepository.getEntityManager().merge(existente);
        return empresaOutputMapper.toDomain(existente);
    }
}
