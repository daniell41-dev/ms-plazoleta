package com.plazoleta.ms_plazoleta.domain.ports.in;

import com.plazoleta.ms_plazoleta.domain.model.Plato;

public interface IPlatoServicePort {
    void guardarPlato(Plato plato, Long propietarioId);
}
