package com.empresa.tomaturno.turno.application.query.port.output;

public interface TurnoConfiguracionPort {
    boolean debeVerificarTurnoActivo(Long idSucursal);

    /** true si el operador está ACTIVA — false si está cerrado, en descanso,
     *  o nunca se ha activado. No se puede llamar un turno si el operador no está activo. */
    boolean operadorActivo(Long idUsuario, Long idSucursal);
}
