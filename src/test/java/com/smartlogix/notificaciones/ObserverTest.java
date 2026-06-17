package com.smartlogix.notificaciones;

import com.smartlogix.notificaciones.observer.EmailObserver;
import com.smartlogix.notificaciones.observer.LogObserver;
import com.smartlogix.notificaciones.observer.NotificacionEventManager;
import com.smartlogix.notificaciones.observer.NotificacionObserver;
import com.smartlogix.notificaciones.observer.PushObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para Observer Pattern
 * Patrón AAA: Arrange - Act - Assert
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - Observer Pattern (ms-notificaciones)")
class ObserverTest {

    private NotificacionEventManager eventManager;
    private EmailObserver emailObserver;
    private LogObserver logObserver;
    private PushObserver pushObserver;

    @Mock
    private NotificacionObserver observerMock;

    @BeforeEach
    void setUp() {
        eventManager  = new NotificacionEventManager();
        emailObserver = new EmailObserver();
        logObserver   = new LogObserver();
        pushObserver  = new PushObserver();
    }

    // ─── PRUEBA 1: Suscribir y notificar un observer ──────────────────────────

    @Test
    @DisplayName("Observer suscrito recibe notificación del evento")
    void testObserverSuscritoRecibeNotificacion() {
        // ARRANGE
        eventManager.suscribir("pedido.confirmado", observerMock);
        Map<String, Object> datos = Map.of(
            "pedidoId", 1,
            "emailCliente", "empresa@pyme.cl",
            "total", 999980
        );

        // ACT
        eventManager.notificar("pedido.confirmado", datos);

        // ASSERT
        verify(observerMock, times(1)).actualizar("pedido.confirmado", datos);
    }

    // ─── PRUEBA 2: Observer desuscrito no recibe notificaciones ──────────────

    @Test
    @DisplayName("Observer desuscrito no recibe notificaciones posteriores")
    void testObserverDesuscritoNoRecibeNotificacion() {
        // ARRANGE
        eventManager.suscribir("pedido.confirmado", observerMock);
        eventManager.desuscribir("pedido.confirmado", observerMock);
        Map<String, Object> datos = Map.of("pedidoId", 1);

        // ACT
        eventManager.notificar("pedido.confirmado", datos);

        // ASSERT
        verify(observerMock, never()).actualizar(any(), any());
    }

    // ─── PRUEBA 3: Múltiples observers reciben el mismo evento ───────────────

    @Test
    @DisplayName("Múltiples observers suscritos reciben el mismo evento")
    void testMultiplesObserversRecibenEvento() {
        // ARRANGE
        NotificacionObserver mock1 = mock(NotificacionObserver.class);
        NotificacionObserver mock2 = mock(NotificacionObserver.class);
        NotificacionObserver mock3 = mock(NotificacionObserver.class);

        eventManager.suscribir("pedido.confirmado", mock1);
        eventManager.suscribir("pedido.confirmado", mock2);
        eventManager.suscribir("pedido.confirmado", mock3);

        Map<String, Object> datos = Map.of("pedidoId", 5);

        // ACT
        eventManager.notificar("pedido.confirmado", datos);

        // ASSERT: los 3 observers fueron notificados exactamente una vez
        verify(mock1, times(1)).actualizar("pedido.confirmado", datos);
        verify(mock2, times(1)).actualizar("pedido.confirmado", datos);
        verify(mock3, times(1)).actualizar("pedido.confirmado", datos);
    }

    // ─── PRUEBA 4: Evento sin observers no lanza excepción ───────────────────

    @Test
    @DisplayName("Notificar evento sin observers suscritos no lanza excepción")
    void testNotificarEventoSinObserversNoLanzaExcepcion() {
        // ARRANGE
        Map<String, Object> datos = Map.of("pedidoId", 99);

        // ACT + ASSERT
        assertDoesNotThrow(() ->
            eventManager.notificar("evento.inexistente", datos)
        );
    }

    // ─── PRUEBA 5: LogObserver registra en historial ─────────────────────────

    @Test
    @DisplayName("LogObserver registra la notificación en su historial interno")
    void testLogObserverRegistraEnHistorial() {
        // ARRANGE
        Map<String, Object> datos = Map.of(
            "pedidoId", 12,
            "emailCliente", "jesus@pyme.cl"
        );

        // ACT
        logObserver.actualizar("pedido.confirmado", datos);

        // ASSERT
        assertFalse(logObserver.getLogs().isEmpty());
        assertTrue(logObserver.getLogs().get(0).contains("pedido.confirmado"));
    }

    // ─── PRUEBA 6: Nombre correcto de cada observer ──────────────────────────

    @Test
    @DisplayName("Cada observer retorna su nombre correcto")
    void testNombreObservers() {
        // ARRANGE + ACT + ASSERT
        assertEquals("EmailObserver", emailObserver.getNombre());
        assertEquals("LogObserver",   logObserver.getNombre());
        assertEquals("PushObserver",  pushObserver.getNombre());
    }

    // ─── PRUEBA 7: Observer suscrito a evento diferente no recibe notificación

    @Test
    @DisplayName("Observer suscrito a otro evento no recibe notificación")
    void testObserverNoRecibeEventoDiferente() {
        // ARRANGE: suscribir solo a pedido.cancelado
        eventManager.suscribir("pedido.cancelado", observerMock);
        Map<String, Object> datos = Map.of("pedidoId", 3);

        // ACT: notificar pedido.confirmado
        eventManager.notificar("pedido.confirmado", datos);

        // ASSERT: el observer NO debe haber recibido nada
        verify(observerMock, never()).actualizar(any(), any());
    }
}