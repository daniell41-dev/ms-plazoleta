package com.plazoleta.ms_plazoleta.application.usecase;

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
import org.mockito.InjectMocks;
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

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private Plato plato;
    private Restaurante restaurante;
    private static final Long PROPIETARIO_ID = 1L;
    private static final Long RESTAURANTE_ID = 10L;
    private static final Long PLATO_ID = 100L;

    @BeforeEach
    void setUp() {
        plato = new Plato(PLATO_ID, "Bandeja Paisa", 25000, "Desc", "url.jpg", "Tradicional", RESTAURANTE_ID);
        restaurante = new Restaurante(RESTAURANTE_ID, "Mi Restaurante", "123456789", "Calle 1", "+571234567", "logo.jpg", PROPIETARIO_ID);
    }

    // ─── guardarPlato ───────────────────────────────────────────────────────────

    @Test
    void guardarPlato_exitoso() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.guardarPlato(plato, PROPIETARIO_ID);

        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_restauranteNoEncontrado_lanzaExcepcion() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.guardarPlato(plato, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_noPropietario_lanzaExcepcion() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.guardarPlato(plato, 99L));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_precioInvalido_lanzaExcepcion() {
        Plato platoConPrecioInvalido = new Plato(PLATO_ID, "Plato", 0, "Desc", "url.jpg", "Cat", RESTAURANTE_ID);
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.guardarPlato(platoConPrecioInvalido, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    // ─── actualizarPlato ────────────────────────────────────────────────────────

    @Test
    void actualizarPlato_exitoso() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.actualizarPlato(PLATO_ID, 30000, "Nueva desc", PROPIETARIO_ID);

        assertEquals(30000, plato.getPrecio());
        assertEquals("Nueva desc", plato.getDescripcion());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void actualizarPlato_soloDescripcion_exitoso() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.actualizarPlato(PLATO_ID, null, "Solo descripcion", PROPIETARIO_ID);

        assertEquals(25000, plato.getPrecio());
        assertEquals("Solo descripcion", plato.getDescripcion());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void actualizarPlato_platoNoEncontrado_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.empty());

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Desc", PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_restauranteNoEncontrado_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Desc", PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_noPropietario_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Desc", 99L));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_precioInvalido_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, -1, "Desc", PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    // ─── habilitarDeshabilitarPlato ─────────────────────────────────────────────

    @Test
    void habilitarDeshabilitarPlato_habilitar_exitoso() {
        plato.setActiva(false);
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, true, PROPIETARIO_ID);

        assertTrue(plato.getActiva());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void habilitarDeshabilitarPlato_deshabilitar_exitoso() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, false, PROPIETARIO_ID);

        assertFalse(plato.getActiva());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void habilitarDeshabilitarPlato_platoNoEncontrado_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.empty());

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, true, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void habilitarDeshabilitarPlato_restauranteNoEncontrado_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, true, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void habilitarDeshabilitarPlato_noPropietario_lanzaExcepcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.habilitarDeshabilitarPlato(PLATO_ID, true, 99L));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }
}
