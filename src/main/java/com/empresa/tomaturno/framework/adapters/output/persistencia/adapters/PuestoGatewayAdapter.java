package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.puesto.application.command.port.output.PuestoGatewayPort;
import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public class PuestoGatewayAdapter implements PuestoGatewayPort {

    private final PuestoQueryRepository puestoQueryRepository;

    public PuestoGatewayAdapter(PuestoQueryRepository puestoQueryRepository) {
        this.puestoQueryRepository = puestoQueryRepository;
    }

    @Override
    public Puesto buscarPorIdPuestoYSucursal(Long idPuesto, Long idSucursal) {
        return puestoQueryRepository.buscarPorIdPuestoYSucursal(idPuesto, idSucursal);
    }

    @Override
    public boolean existeNombreEnSucursal(Long idSucursal, String nombre) {
        return puestoQueryRepository.existeNombreEnSucursal(idSucursal, nombre);
    }
}
