package com.plazoleta.ms_plazoleta.domain.ports.out;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import java.util.List;
import java.util.Optional;

public interface IRestaurantePersistencePort {

    void guardarRestaurante(Restaurante restaurante);

    Optional<Restaurante> buscarRestaurantePorId(Long id);

    List<Restaurante> listarRestaurantes(int pagina, int tamano);
}
