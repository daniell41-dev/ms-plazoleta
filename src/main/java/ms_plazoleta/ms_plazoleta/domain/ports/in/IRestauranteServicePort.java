package ms_plazoleta.ms_plazoleta.domain.ports.in;

import ms_plazoleta.ms_plazoleta.domain.model.Restaurante;

public interface IRestauranteServicePort {

    void guardarRestaurante(Restaurante restaurante);
}
