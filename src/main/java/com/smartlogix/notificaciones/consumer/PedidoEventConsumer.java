package com.smartlogix.notificaciones.consumer;

import com.smartlogix.notificaciones.observer.NotificacionEventManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PedidoEventConsumer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificacionEventManager eventManager;

    public PedidoEventConsumer(RedisTemplate<String, Object> redisTemplate,
                                NotificacionEventManager eventManager) {
        this.redisTemplate = redisTemplate;
        this.eventManager = eventManager;
    }

    @KafkaListener(topics = "pedido.confirmado", groupId = "notificaciones-group")
    public void onPedidoConfirmado(Map<String, Object> evento) {
        System.out.println("📨 Evento recibido: pedido.confirmado");

        // Notificar a los observadores
        eventManager.notificar("pedido.confirmado", evento);

        //  Almacenar en Redis para seguimiento rápido
        String key = "notif:pedido:" + evento.get("pedidoId");
        redisTemplate.opsForValue().set(key,
            "Confirmado el " + LocalDateTime.now() + " | Total: $" + evento.get("total"),
            24, TimeUnit.HOURS
        );
    }

    @KafkaListener(topics = "pedido.cancelado", groupId = "notificaciones-group")
    public void onPedidoCancelado(Map<String, Object> evento) {
        System.out.println("❌ Evento recibido: pedido.cancelado");
        eventManager.notificar("pedido.cancelado", evento);
    }
}