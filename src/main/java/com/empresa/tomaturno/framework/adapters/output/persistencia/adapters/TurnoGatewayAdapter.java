package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.estadooperador.application.query.port.output.EstadoOperadorQueryRepository;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.estadooperador.dominio.vo.DetalleEstadoOperador;
import com.empresa.tomaturno.shared.clases.ConfiguracionClave;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoGatewayPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

public class TurnoGatewayAdapter implements TurnoGatewayPort {

    private final TurnoQueryRepository turnoQueryRepository;
    private final ColaQueryRepository colaQueryRepository;
    private final ConfiguracionQueryRepository configuracionQueryRepository;
    private final EstadoOperadorQueryRepository estadoOperadorQueryRepository;

    public TurnoGatewayAdapter(TurnoQueryRepository turnoQueryRepository,
            ColaQueryRepository colaQueryRepository,
            ConfiguracionQueryRepository configuracionQueryRepository,
            EstadoOperadorQueryRepository estadoOperadorQueryRepository) {
        this.turnoQueryRepository = turnoQueryRepository;
        this.colaQueryRepository = colaQueryRepository;
        this.configuracionQueryRepository = configuracionQueryRepository;
        this.estadoOperadorQueryRepository = estadoOperadorQueryRepository;
    }

    @Override
    public Turno buscarPorPK(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
    }

    @Override
    public List<Turno> buscarPorFiltro(Long idSucursal, Long idCola, Long idDetalle, Integer estado, LocalDate fecha,
            Long idPuesto, Long idSucursalPuesto) {
        return turnoQueryRepository.buscarPorFiltro(idSucursal, idCola, idDetalle, estado, fecha, idPuesto,
                idSucursalPuesto);
    }

    @Override
    public Long obtenerSiguienteNumero(Long idSucursal, LocalDate fecha, String codigoBase) {
        return turnoQueryRepository.obtenerSiguienteNumero(idSucursal, fecha, codigoBase);
    }

    @Override
    public Long obtenerSiguienteId() {
        return turnoQueryRepository.obtenerSiguienteId();
    }

    @Override
    public boolean existeTurnoLlamadoPorPuesto(Long idPuesto, Long idSucursal, LocalDate fecha) {
        return turnoQueryRepository.existeTurnoLlamadoPorPuesto(idPuesto, idSucursal, fecha);
    }

    @Override
    public boolean existeTurnoLlamadoPorUsuario(Long idUsuario, Long idSucursal, LocalDate fecha) {
        return turnoQueryRepository.existeTurnoLlamadoPorUsuario(idUsuario, idSucursal, fecha);
    }

    @Override
    public String obtenerCodigoBaseTurno(Long idSucursal, Long idCola, Long idDetalle) {
        return turnoQueryRepository.obtenerCodigoBaseTurno(idSucursal, idCola, idDetalle);
    }

    @Override
    public Long obtenerCorreltivoDetalle(Long idSucursal, Long idCola, Long idDetalle) {
        return turnoQueryRepository.obtenerCorreltivoDetalle(idSucursal, idCola, idDetalle);
    }

    @Override
    public Long resolverDetalleParaReasignacion(Long idCola, Long idSucursal, Long idDetalle) {
        Cola cola = colaQueryRepository.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
        if (cola == null) {
            return null;
        }
        return cola.resolverDetalleReasignacion(idDetalle);
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
