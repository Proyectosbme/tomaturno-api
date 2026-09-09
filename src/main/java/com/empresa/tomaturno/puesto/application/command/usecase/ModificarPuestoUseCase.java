package com.empresa.tomaturno.puesto.application.command.usecase;

import com.empresa.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.empresa.tomaturno.puesto.application.command.port.output.PuestoGatewayPort;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.exceptions.PuestoNotFoundException;
import com.empresa.tomaturno.puesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.puesto.dominio.vo.Estado;

public class ModificarPuestoUseCase {

    private final PuestoCommandRepository puestoCommandRepository;
    private final PuestoGatewayPort puestoGatewayPort;

    public ModificarPuestoUseCase(PuestoCommandRepository puestoCommandRepository,
            PuestoGatewayPort puestoGatewayPort) {
        this.puestoCommandRepository = puestoCommandRepository;
        this.puestoGatewayPort = puestoGatewayPort;
    }

    public Puesto ejecutar(Long idPuesto, Long idSucursal, String nombre, String nombreLlamada, Estado estado,
            Auditoria auditoriaModificacion) {
        Puesto puesto = puestoGatewayPort.buscarPorIdPuestoYSucursal(idPuesto, idSucursal);
        if (puesto == null) {
            throw new PuestoNotFoundException(idPuesto,
                    "Puesto (idPuesto=" + idPuesto + ", idSucursal=" + idSucursal + ")");
        }
        puesto.modificar(nombre, nombreLlamada, estado, auditoriaModificacion);
        return puestoCommandRepository.modificar(puesto);
    }
}
