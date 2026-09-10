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

@ServerEndpoint("/turnos")
@ApplicationScoped
@Startup
public class TurnoWebSocket {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    /** idUsuario -> sesiones abiertas de ese operador (puede tener más de una pestaña). */
    private static final Map<Long, Set<Session>> sesionesPorUsuario = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        Long idUsuario = extraerIdUsuario(session);
        if (idUsuario != null) {
            sesionesPorUsuario.computeIfAbsent(idUsuario, k -> new CopyOnWriteArraySet<>()).add(session);
        }
        System.out.println("Cliente conectado: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        Long idUsuario = extraerIdUsuario(session);
        if (idUsuario != null) {
            sesionesPorUsuario.computeIfPresent(idUsuario, (k, sesiones) -> {
                sesiones.remove(session);
                return sesiones.isEmpty() ? null : sesiones;
            });
        }
        System.out.println("Cliente desconectado: " + session.getId());
    }

    private Long extraerIdUsuario(Session session) {
        List<String> valores = session.getRequestParameterMap().get("idUsuario");
        if (valores == null || valores.isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(valores.get(0));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** true si el operador tiene al menos una pestaña/sesión de WebSocket abierta ahora mismo. */
    public boolean tieneSesionActiva(Long idUsuario) {
        if (idUsuario == null) {
            return false;
        }
        Set<Session> sesiones = sesionesPorUsuario.get(idUsuario);
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
