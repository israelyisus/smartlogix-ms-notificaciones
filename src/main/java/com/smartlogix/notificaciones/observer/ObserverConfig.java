package com.smartlogix.notificaciones.observer;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class ObserverConfig {

    private final NotificacionEventManager eventManager;
    private final EmailObserver emailObserver;
    private final LogObserver logObserver;
    private final PushObserver pushObserver;

    public ObserverConfig(NotificacionEventManager eventManager,
                          EmailObserver emailObserver,
                          LogObserver logObserver,
                          PushObserver pushObserver) {
        this.eventManager = eventManager;
        this.emailObserver = emailObserver;
        this.logObserver = logObserver;
        this.pushObserver = pushObserver;
    }

    @PostConstruct
    public void configurarSuscripciones() {

        // pedido.confirmado → los 3 observers
        eventManager.suscribir("pedido.confirmado", emailObserver);
        eventManager.suscribir("pedido.confirmado", logObserver);
        eventManager.suscribir("pedido.confirmado", pushObserver);

        // pedido.cancelado → solo email y log
        eventManager.suscribir("pedido.cancelado", emailObserver);
        eventManager.suscribir("pedido.cancelado", logObserver);

        System.out.println("🚀Sistema de notificaciones configurado");
    }
}