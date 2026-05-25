package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.shared.clases.ConfiguracionClave;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoConfiguracionPort;

public class TurnoConfiguracionAdapter implements TurnoConfiguracionPort {

    private final ConfiguracionQueryRepository configuracionQueryRepository;

    public TurnoConfiguracionAdapter(ConfiguracionQueryRepository configuracionQueryRepository) {
        this.configuracionQueryRepository = configuracionQueryRepository;
    }

    @Override
    public boolean debeVerificarTurnoActivo(Long idSucursal) {
        Configuracion config = configuracionQueryRepository.buscarPorNombreYSucursal(
                idSucursal, ConfiguracionClave.LLAMAR_CON_ACTIVO.clave());
        return config != null && Integer.valueOf(0).equals(config.getParametro());
    }
}
