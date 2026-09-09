package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.estadooperador.application.query.port.output.EstadoOperadorQueryRepository;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleEstadoOperador;
import com.empresa.tomaturno.shared.clases.ConfiguracionClave;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoConfiguracionPort;

public class TurnoConfiguracionAdapter implements TurnoConfiguracionPort {

    private final ConfiguracionQueryRepository configuracionQueryRepository;
    private final EstadoOperadorQueryRepository estadoOperadorQueryRepository;

    public TurnoConfiguracionAdapter(ConfiguracionQueryRepository configuracionQueryRepository,
            EstadoOperadorQueryRepository estadoOperadorQueryRepository) {
        this.configuracionQueryRepository = configuracionQueryRepository;
        this.estadoOperadorQueryRepository = estadoOperadorQueryRepository;
    }

    @Override
    public boolean debeVerificarTurnoActivo(Long idSucursal) {
        Configuracion config = configuracionQueryRepository.buscarPorNombreYSucursal(
                idSucursal, ConfiguracionClave.LLAMAR_CON_ACTIVO.clave());
        return config != null && Integer.valueOf(0).equals(config.getParametro());
    }

    @Override
    public boolean operadorActivo(Long idUsuario, Long idSucursal) {
        EstadoOperador vigente = estadoOperadorQueryRepository.buscarVigente(idUsuario, idSucursal);
        return vigente != null && vigente.getIdEstadoOperador() != null
                && vigente.getIdEstadoOperador() == DetalleEstadoOperador.ACTIVA.getValor();
    }
}
