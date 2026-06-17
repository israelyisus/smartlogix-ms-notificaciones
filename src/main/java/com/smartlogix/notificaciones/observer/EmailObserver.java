package com.smartlogix.notificaciones.observer;

import org.springframework.stereotype.Component;

@Component
public class EmailObserver implements NotificacionObserver {

    @Override
    public void actualizar(String evento, Object datos) {
        System.out.println("📧Evento recibido: " + evento);
        System.out.println("   → Enviando email con datos: " + datos.toString());
    }

    @Override
    public String getNombre() {
        return "EmailObserver";
    }
}