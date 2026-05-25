package com.empresa.tomaturno.turno.application.query.port.output;

public interface TurnoConfiguracionPort {
    boolean debeVerificarTurnoActivo(Long idSucursal);
}
