# SmartLogix — ms-notificaciones

Microservicio de notificaciones. Distribuye alertas a múltiples canales cuando ocurren eventos relevantes en el sistema, como la confirmación de un pedido.

## Tecnologías

- Java 17
- Spring Boot 3.5.14
- Apache Kafka (consumidor)
- JUnit 5 + Mockito

## Patrón de diseño implementado

**Observer** — `observer/NotificacionEventManager.java` actúa como Subject que mantiene una lista de observadores (`EmailObserver`, `LogObserver`, `PushObserver`) y los notifica automáticamente cuando llega un evento desde Kafka, sin que el emisor del evento conozca a los observadores.

## Requisitos

- Java 17 (JDK)
- Maven (incluido como `mvnw`)
- Apache Kafka y Zookeeper corriendo (recomendado usar Docker Compose desde el repositorio principal)

## Configuración

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: smartlogix-notificaciones
      auto-offset-reset: earliest
```

## Instalación y ejecución

```bash
./mvnw clean install
./mvnw spring-boot:run
```

El microservicio queda disponible en `http://localhost:8085`.

## Cómo ejecutar las pruebas

```bash
./mvnw test
```

Se ejecutan 7 pruebas unitarias sobre `NotificacionEventManager`, verificando la suscripción, desuscripción y notificación a múltiples observadores.

## Cómo funciona

1. `ms-pedidos` confirma un pedido y publica un evento en el topic `pedido.confirmado` de Kafka.
2. `ms-notificaciones` consume el evento mediante un `@KafkaListener`.
3. El evento se pasa a `NotificacionEventManager.notificar()`.
4. Cada observador suscrito reacciona de forma independiente:
   - `EmailObserver` simula el envío de un correo de confirmación.
   - `LogObserver` registra el evento en un historial interno.
   - `PushObserver` simula una notificación push al navegador.

## Estructura del proyecto

```
ms-notificaciones/
├── src/main/java/com/smartlogix/notificaciones/
│   ├── kafka/             KafkaListener que consume eventos
│   ├── observer/           NotificacionEventManager + 3 Observers (Observer Pattern)
│   └── config/             ObserverConfig (registra las suscripciones)
├── src/test/java/com/smartlogix/notificaciones/
│   └── ObserverTest.java
├── Dockerfile
└── pom.xml
```
