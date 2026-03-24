package com.plazoleta.ms_plazoleta.usecase;

import com.plazoleta.ms_plazoleta.application.usecase.PlatoUseCase;
import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioDelRestauranteException;
import com.plazoleta.ms_plazoleta.domain.exception.PlatoNoEncontradoException;
import com.plazoleta.ms_plazoleta.domain.exception.RestauranteNoEncontradoException;
import com.plazoleta.ms_plazoleta.domain.model.Plato;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.out.IPlatoPersistencePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    private PlatoUseCase platoUseCase;

    private static final Long PROPIETARIO_ID = 1L;
    private static final Long OTRO_PROPIETARIO_ID = 99L;
    private static final Long RESTAURANTE_ID = 10L;
    private static final Long PLATO_ID = 5L;

    @BeforeEach
    void setUp() {
        platoUseCase = new PlatoUseCase(platoPersistencePort, restaurantePersistencePort);
    }

    private Restaurante restaurante(Long propietarioId) {
        return new Restaurante(RESTAURANTE_ID, "Restaurante Test", "123456789", "Calle 1", "3001234567", "http://logo.com", propietarioId);
    }

    private Plato plato() {
        return new Plato(PLATO_ID, "Bandeja Paisa", 25000, "Plato típico", "http://img.com", "Típica", RESTAURANTE_ID);
    }

    // ─── guardarPlato ────────────────────────────────────────────────────────

    @Test
    void guardarPlato_exitoso() {
        Plato plato = plato();
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(PROPIETARIO_ID)));

        assertDoesNotThrow(() -> platoUseCase.guardarPlato(plato, PROPIETARIO_ID));
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_restauranteNoExiste_lanzaExcepcion() {
        Plato plato = plato();
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.guardarPlato(plato, PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_propietarioNoEsDuenio_lanzaExcepcion() {
        Plato plato = plato();
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(OTRO_PROPIETARIO_ID)));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.guardarPlato(plato, PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_precioMenorOIgualCero_lanzaExcepcion() {
        Plato plato = new Plato(null, "Bandeja", 0, "Desc", "http://img.com", "Típica", RESTAURANTE_ID);
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(PROPIETARIO_ID)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.guardarPlato(plato, PROPIETARIO_ID));
        assertEquals("El precio debe ser mayor a 0", ex.getMessage());
    }

    // ─── actualizarPlato ──────────────────────────────────────────────────────

    @Test
    void actualizarPlato_exitoso_actualizaPrecioYDescripcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato()));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(PROPIETARIO_ID)));

        assertDoesNotThrow(() -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Nueva descripción", PROPIETARIO_ID));
        verify(platoPersistencePort).guardarPlato(any());
    }

    @Test
    void actualizarPlato_soloDescripcion_exitoso() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato()));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(PROPIETARIO_ID)));

        assertDoesNotThrow(() -> platoUseCase.actualizarPlato(PLATO_ID, null, "Solo descripción", PROPIETARIO_ID));
        verify(platoPersistencePort).guardarPlato(any());
    }

    @Test
    void actualizarPlato_platoNoExiste_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.empty());

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Desc", PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_restauranteNoExiste_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato()));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Desc", PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_propietarioNoEsDuenio_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato()));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(OTRO_PROPIETARIO_ID)));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Desc", PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_precioInvalido_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato()));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(PROPIETARIO_ID)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, -100, "Desc", PROPIETARIO_ID));
        assertEquals("El precio debe ser mayor a 0", ex.getMessage());
    }

    // ─── habilitarDeshabilitarPlato ───────────────────────────────────────────

    @Test
    void habilitarDeshabilitarPlato_deshabilitar_exitoso() {
        Plato plato = plato();
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(PROPIETARIO_ID)));

        assertDoesNotThrow(() -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, false, PROPIETARIO_ID));

        assertFalse(plato.getActiva());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void habilitarDeshabilitarPlato_habilitar_exitoso() {
        Plato plato = plato();
        plato.setActiva(false);
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(PROPIETARIO_ID)));

        assertDoesNotThrow(() -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, true, PROPIETARIO_ID));

        assertTrue(plato.getActiva());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void habilitarDeshabilitarPlato_platoNoExiste_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.empty());

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, false, PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void habilitarDeshabilitarPlato_restauranteNoExiste_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato()));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, false, PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void habilitarDeshabilitarPlato_propietarioNoEsDuenio_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID))
                .thenReturn(Optional.of(plato()));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante(OTRO_PROPIETARIO_ID)));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, false, PROPIETARIO_ID));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }
}
