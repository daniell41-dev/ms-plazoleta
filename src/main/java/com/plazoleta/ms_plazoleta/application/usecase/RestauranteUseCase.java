package com.plazoleta.ms_plazoleta.application.usecase;

import com.plazoleta.ms_plazoleta.domain.constants.RestauranteConstantes;
import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioException;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.in.IRestauranteServicePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IUsuarioServicePort;

public class RestauranteUseCase implements IRestauranteServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IUsuarioServicePort usuarioServicePort;

    public RestauranteUseCase(IRestaurantePersistencePort restaurantePersistencePort,
                              IUsuarioServicePort usuarioServicePort) {
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.usuarioServicePort = usuarioServicePort;
    }

    @Override
    public void guardarRestaurante(Restaurante restaurante) {

        // El propietario debe tener rol PROPIETARIO en ms-usuario
        String rol = usuarioServicePort.obtenerRolUsuario(restaurante.getPropietarioId());
        if (!RestauranteConstantes.ROL_PROPIETARIO.equals(rol)) {
            throw new NoPropietarioException("El usuario no tiene rol de propietario");
        }

        // El NIT debe ser únicamente numérico
        if (!restaurante.getNit().matches(RestauranteConstantes.PATRON_SOLO_DIGITOS)) {
            throw new IllegalArgumentException("El NIT debe ser únicamente numérico");
        }

        // El teléfono debe ser numérico (permite + al inicio), máximo 13 caracteres
        if (!restaurante.getTelefono().matches(RestauranteConstantes.PATRON_TELEFONO)) {
            throw new IllegalArgumentException("El teléfono debe ser únicamente numérico");
        }
        if (restaurante.getTelefono().length() > RestauranteConstantes.LONGITUD_MAXIMA_TELEFONO) {
            throw new IllegalArgumentException("El teléfono no puede tener más de 13 caracteres");
        }

        // El nombre no puede ser solo números
        if (restaurante.getNombre().matches(RestauranteConstantes.PATRON_SOLO_DIGITOS)) {
            throw new IllegalArgumentException("El nombre no puede ser únicamente numérico");
        }

        restaurantePersistencePort.guardarRestaurante(restaurante);
    }
}
