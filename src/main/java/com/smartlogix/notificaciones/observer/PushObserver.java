package com.smartlogix.notificaciones.observer;

import org.springframework.stereotype.Component;

@Component
public class PushObserver implements NotificacionObserver {

    @Override
    public void actualizar(String evento, Object datos) {
        System.out.println("🔔 Notificación push enviada:");
        System.out.println("   → Evento: " + evento);
        System.out.println("   → Datos: " + datos.toString());
    }

    @Override
    public String getNombre() {
        return "PushObserver";
    }
}