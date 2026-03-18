package com.plazoleta.ms_plazoleta.domain.ports.out;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;

public interface IRestaurantePersistencePort {

    void guardarRestaurante(Restaurante restaurante);
}
