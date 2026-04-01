package com.plazoleta.ms_plazoleta.application.usecase;

import com.plazoleta.ms_plazoleta.domain.constants.RestauranteConstantes;
import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioException;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IUsuarioServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IUsuarioServicePort usuarioServicePort;

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    private Restaurante restauranteValido;

    @BeforeEach
    void setUp() {
        restauranteValido = new Restaurante(
                null,
                "La Fogata",
                "123456789",
                "Calle 123",
                "+573001234567",
                "http://logo.com/img.png",
                1L
        );
    }

    // ─── Camino feliz ────────────────────────────────────────────────────────

    @Test
    void guardarRestaurante_cuandoTodosLosDatosSonValidos_guardaCorrectamente() {
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        restauranteUseCase.guardarRestaurante(restauranteValido);

        verify(restaurantePersistencePort, times(1)).guardarRestaurante(restauranteValido);
    }

    @Test
    void guardarRestaurante_cuandoTelefonoSinPlusEsValido_guardaCorrectamente() {
        Restaurante restaurante = new Restaurante(
                null, "La Fogata", "123456789", "Calle 123",
                "3001234567", "http://logo.com/img.png", 1L
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        restauranteUseCase.guardarRestaurante(restaurante);

        verify(restaurantePersistencePort, times(1)).guardarRestaurante(restaurante);
    }

    // ─── Validación de rol ───────────────────────────────────────────────────

    @Test
    void guardarRestaurante_cuandoRolNoEsPropietario_lanzaNoPropietarioException() {
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("EMPLEADO");

        assertThrows(NoPropietarioException.class,
                () -> restauranteUseCase.guardarRestaurante(restauranteValido));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_cuandoRolEsCliente_lanzaNoPropietarioException() {
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("CLIENTE");

        assertThrows(NoPropietarioException.class,
                () -> restauranteUseCase.guardarRestaurante(restauranteValido));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    // ─── Validación de NIT ───────────────────────────────────────────────────

    @Test
    void guardarRestaurante_cuandoNitTieneLetras_lanzaIllegalArgumentException() {
        Restaurante restaurante = new Restaurante(
                null, "La Fogata", "12ABC789", "Calle 123",
                "+573001234567", "http://logo.com/img.png", 1L
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_cuandoNitTieneGuion_lanzaIllegalArgumentException() {
        Restaurante restaurante = new Restaurante(
                null, "La Fogata", "123-456", "Calle 123",
                "+573001234567", "http://logo.com/img.png", 1L
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    // ─── Validación de teléfono ──────────────────────────────────────────────

    @Test
    void guardarRestaurante_cuandoTelefonoTieneLetras_lanzaIllegalArgumentException() {
        Restaurante restaurante = new Restaurante(
                null, "La Fogata", "123456789", "Calle 123",
                "30012ABC67", "http://logo.com/img.png", 1L
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_cuandoTelefonoSuperaLos13Caracteres_lanzaIllegalArgumentException() {
        Restaurante restaurante = new Restaurante(
                null, "La Fogata", "123456789", "Calle 123",
                "+5730012345678", "http://logo.com/img.png", 1L  // 14 chars
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_cuandoTelefonoTieneExactamente13Caracteres_guardaCorrectamente() {
        Restaurante restaurante = new Restaurante(
                null, "La Fogata", "123456789", "Calle 123",
                "+573001234567", "http://logo.com/img.png", 1L  // 13 chars exactos
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        restauranteUseCase.guardarRestaurante(restaurante);

        verify(restaurantePersistencePort, times(1)).guardarRestaurante(restaurante);
    }

    // ─── Validación de nombre ────────────────────────────────────────────────

    @Test
    void guardarRestaurante_cuandoNombreEsSoloNumeros_lanzaIllegalArgumentException() {
        Restaurante restaurante = new Restaurante(
                null, "123456", "123456789", "Calle 123",
                "+573001234567", "http://logo.com/img.png", 1L
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_cuandoNombreTieneNumerosYLetras_guardaCorrectamente() {
        Restaurante restaurante = new Restaurante(
                null, "Restaurante 99", "123456789", "Calle 123",
                "+573001234567", "http://logo.com/img.png", 1L
        );
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn(RestauranteConstantes.ROL_PROPIETARIO);

        restauranteUseCase.guardarRestaurante(restaurante);

        verify(restaurantePersistencePort, times(1)).guardarRestaurante(restaurante);
    }
}
