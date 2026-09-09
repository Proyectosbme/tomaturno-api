package com.empresa.tomaturno.cola.application.command.usecase;

import java.util.List;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.port.output.ColaGatewayPort;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.especificacion.CodigoColaUnicoEspec;
import com.empresa.tomaturno.cola.dominio.especificacion.NombreColaUnicoEspec;

public class CrearColaCaseUse {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaGatewayPort colaGatewayPort;

    public CrearColaCaseUse(ColaCommandRepository colaCommandRepository,
            ColaGatewayPort colaGatewayPort) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaGatewayPort = colaGatewayPort;
    }

    public Cola ejecutar(Cola cola) {
        List<Cola> colasExistentes = colaGatewayPort
                .buscarConDetallesPorSucursal(cola.getSucursal().getIdentificador());
        new NombreColaUnicoEspec(colasExistentes).verificar(cola.getNombre());
        new CodigoColaUnicoEspec(colasExistentes).verificar(cola.getCodigo());
        return colaCommandRepository.save(cola);
    }
}
