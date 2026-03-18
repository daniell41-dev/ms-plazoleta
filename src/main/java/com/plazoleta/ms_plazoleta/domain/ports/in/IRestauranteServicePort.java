package com.plazoleta.ms_plazoleta.domain.ports.in;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;

public interface IRestauranteServicePort {

    void guardarRestaurante(Restaurante restaurante);
}
