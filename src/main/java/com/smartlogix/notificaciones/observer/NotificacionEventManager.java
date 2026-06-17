package com.smartlogix.notificaciones.observer;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificacionEventManager {

    // Mapa de evento → lista de observers suscritos
    private final Map<String, List<NotificacionObserver>> listeners = new HashMap<>();

    public void suscribir(String evento, NotificacionObserver observer) {
        listeners.computeIfAbsent(evento, k -> new ArrayList<>()).add(observer);
        System.out.println("✅" + observer.getNombre()
            + " suscrito al evento: " + evento);
    }

    public void desuscribir(String evento, NotificacionObserver observer) {
        List<NotificacionObserver> obs = listeners.get(evento);
        if (obs != null) {
            obs.remove(observer);
            System.out.println("🗑️ " + observer.getNombre()
                + " desuscrito del evento: " + evento);
        }
    }

    public void notificar(String evento, Object datos) {
        List<NotificacionObserver> obs = listeners.getOrDefault(evento, new ArrayList<>());
        System.out.println("📣 Notificando " + obs.size()
            + " observer(s) para evento: " + evento);
        obs.forEach(o -> o.actualizar(evento, datos));
    }
}