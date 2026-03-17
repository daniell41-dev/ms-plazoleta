package ms_plazoleta.ms_plazoleta.domain.ports.out;

import ms_plazoleta.ms_plazoleta.domain.model.Restaurante;

public interface IRestaurantePersistencePort {

    void guardarRestaurante(Restaurante restaurante);
}
