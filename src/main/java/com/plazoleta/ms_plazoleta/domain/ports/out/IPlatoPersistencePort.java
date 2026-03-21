package com.plazoleta.ms_plazoleta.domain.ports.out;

import com.plazoleta.ms_plazoleta.domain.model.Plato;

public interface IPlatoPersistencePort
{
    void guardarPlato(Plato plato);
}
