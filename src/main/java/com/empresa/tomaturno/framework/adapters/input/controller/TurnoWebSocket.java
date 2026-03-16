package com.empresa.tomaturno.framework.adapters.input.controller;

import java.io.IOException;
import java.util.Set;
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

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("Cliente conectado: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Cliente desconectado: " + session.getId());
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
