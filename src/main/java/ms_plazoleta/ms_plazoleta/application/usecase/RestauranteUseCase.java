package ms_plazoleta.ms_plazoleta.application.usecase;

import ms_plazoleta.ms_plazoleta.domain.model.Restaurante;
import ms_plazoleta.ms_plazoleta.domain.ports.in.IRestauranteServicePort;
import ms_plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;

public class RestauranteUseCase implements IRestauranteServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;

    public RestauranteUseCase(IRestaurantePersistencePort restaurantePersistencePort) {
        this.restaurantePersistencePort = restaurantePersistencePort;
    }

    @Override
    public void guardarRestaurante(Restaurante restaurante) {

        // El NIT debe ser únicamente numérico
        if (!restaurante.getNit().matches("\\d+")) {
            throw new IllegalArgumentException("El NIT debe ser únicamente numérico");
        }

        // El teléfono debe ser numérico (permite + al inicio), máximo 13 caracteres
        if (!restaurante.getTelefono().matches("\\+?\\d+")) {
            throw new IllegalArgumentException("El teléfono debe ser únicamente numérico");
        }
        if (restaurante.getTelefono().length() > 13) {
            throw new IllegalArgumentException("El teléfono no puede tener más de 13 caracteres");
        }

        // El nombre no puede ser solo números
        if (restaurante.getNombre().matches("\\d+")) {
            throw new IllegalArgumentException("El nombre no puede ser únicamente numérico");
        }

        restaurantePersistencePort.guardarRestaurante(restaurante);
    }
}
