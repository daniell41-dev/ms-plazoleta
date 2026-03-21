package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper;

import com.plazoleta.ms_plazoleta.domain.model.Plato;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity.PlatoEntity;

public interface IPlatoEntityMapper {

    PlatoEntity toEntity(Plato plato);

    Plato toPlato(PlatoEntity platoEntity);

}
