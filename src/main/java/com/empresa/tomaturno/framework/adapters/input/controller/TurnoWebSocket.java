package com.empresa.tomaturno.framework.adapters.input.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import io.quarkus.runtime.Startup;

import com.empresa.tomaturno.framework.adapters.config.EstadoOperadorAutomaticoOrquestador;

@ServerEndpoint("/turnos")
@ApplicationScoped
@Startup
public class TurnoWebSocket {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    /**
     * (idUsuario, idSucursal) -> sesiones abiertas de ese operador (puede tener más de una
     * pestaña). El id de usuario es correlativo POR sucursal (ver
     * UsuarioJpaRepository.obtenerSiguienteId), no global: el mismo número puede
     * pertenecer a personas distintas en sucursales distintas. Se necesitan ambos datos
     * para identificar a un operador sin ambigüedad.
     */
    private static final Map<ClaveOperador, Set<Session>> sesionesPorUsuario = new ConcurrentHashMap<>();

    private record ClaveOperador(Long idUsuario, Long idSucursal) {
    }

    private final EstadoOperadorAutomaticoOrquestador estadoOperadorAutomaticoOrquestador;

    public TurnoWebSocket(EstadoOperadorAutomaticoOrquestador estadoOperadorAutomaticoOrquestador) {
        this.estadoOperadorAutomaticoOrquestador = estadoOperadorAutomaticoOrquestador;
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        Long idUsuario = extraerIdUsuario(session);
        Long idSucursal = extraerIdSucursal(session);
        if (idUsuario != null && idSucursal != null) {
            sesionesPorUsuario.computeIfAbsent(new ClaveOperador(idUsuario, idSucursal),
                    k -> new CopyOnWriteArraySet<>()).add(session);
        }
        // Si estaba en el descanso automático por desconexión, lo regresa a ACTIVA solo.
        estadoOperadorAutomaticoOrquestador.operadorReconectado(idUsuario, idSucursal);
        System.out.println("Cliente conectado: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        Long idUsuario = extraerIdUsuario(session);
        Long idSucursal = extraerIdSucursal(session);
        if (idUsuario != null && idSucursal != null) {
            sesionesPorUsuario.computeIfPresent(new ClaveOperador(idUsuario, idSucursal), (k, sesiones) -> {
                sesiones.remove(session);
                return sesiones.isEmpty() ? null : sesiones;
            });
        }
        // Si esta era su última pestaña abierta, programa la revisión de "sesión cerrada"
        // (con margen de tolerancia, ver EstadoOperadorAutomaticoOrquestador).
        if (!tieneSesionActiva(idUsuario, idSucursal)) {
            estadoOperadorAutomaticoOrquestador.operadorDesconectado(idUsuario, idSucursal,
                    () -> tieneSesionActiva(idUsuario, idSucursal));
        }
        System.out.println("Cliente desconectado: " + session.getId());
    }

    private Long extraerIdUsuario(Session session) {
        return extraerParametroLong(session, "idUsuario");
    }

    /** Sucursal del operador, enviada por el front junto a idUsuario (ver TurnoWebSocketApi.connect). */
    private Long extraerIdSucursal(Session session) {
        return extraerParametroLong(session, "idSucursal");
    }

    private Long extraerParametroLong(Session session, String nombreParametro) {
        List<String> valores = session.getRequestParameterMap().get(nombreParametro);
        if (valores == null || valores.isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(valores.get(0));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * true si el operador (idUsuario + idSucursal, no idUsuario solo — ver el comentario
     * de sesionesPorUsuario) tiene al menos una pestaña/sesión de WebSocket abierta ahora
     * mismo.
     */
    public boolean tieneSesionActiva(Long idUsuario, Long idSucursal) {
        if (idUsuario == null || idSucursal == null) {
            return false;
        }
        Set<Session> sesiones = sesionesPorUsuario.get(new ClaveOperador(idUsuario, idSucursal));
        return sesiones != null && sesiones.stream().anyMatch(Session::isOpen);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Mensaje recibido: " + message);
        enviarTurno(message);
    }

    public void enviarTurno(String turno) {
        for (Session s : sessions) {
            s.getAsyncRemote().sendText(turno);
        }
    }
}
