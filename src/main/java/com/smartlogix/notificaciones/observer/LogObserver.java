package com.smartlogix.notificaciones.observer;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class LogObserver implements NotificacionObserver {

    private final List<String> logs = new ArrayList<>();

    @Override
    public void actualizar(String evento, Object datos) {
        String entrada = "[" + LocalDateTime.now() + "] " + evento + " → " + datos.toString();
        logs.add(entrada);
        System.out.println("📋" + entrada);
    }

    @Override
    public String getNombre() {
        return "LogObserver";
    }

    public List<String> getLogs() {
        return logs;
    }
}