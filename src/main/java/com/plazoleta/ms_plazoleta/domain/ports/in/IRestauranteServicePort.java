package com.plazoleta.ms_plazoleta.domain.ports.in;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import java.util.List;

public interface IRestauranteServicePort {

    void guardarRestaurante(Restaurante restaurante);

    List<Restaurante> listarRestaurantes(int pagina, int tamano);
}
