package com.smartlogix.notificaciones.observer;

public interface NotificacionObserver {
    void actualizar(String evento, Object datos);
    String getNombre();
}