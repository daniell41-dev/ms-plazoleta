package com.plazoleta.ms_plazoleta.domain.ports.out;

import com.plazoleta.ms_plazoleta.domain.model.Plato;

import java.util.Optional;

public interface IPlatoPersistencePort
{
    void guardarPlato(Plato plato);

    Optional<Plato> buscarPlatoPorId(Long id);
}
