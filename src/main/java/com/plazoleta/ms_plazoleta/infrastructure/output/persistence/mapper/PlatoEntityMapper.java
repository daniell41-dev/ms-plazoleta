package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper;

import com.plazoleta.ms_plazoleta.domain.model.Plato;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity.PlatoEntity;
import org.springframework.stereotype.Component;

@Component
public class PlatoEntityMapper implements IPlatoEntityMapper{

    @Override
    public PlatoEntity toEntity(Plato plato){
        return new PlatoEntity(
                plato.getId(),
                plato.getNombre(),
                plato.getPrecio(),
                plato.getDescripcion(),
                plato.getUrlImagen(),
                plato.getCategoria(),
                plato.getActiva(),
                plato.getRestauranteId()
        );
    }

    @Override
    public Plato toPlato(PlatoEntity platoEntity){
        return new Plato(
                platoEntity.getId(),
                platoEntity.getNombre(),
                platoEntity.getPrecio(),
                platoEntity.getDescripcion(),
                platoEntity.getUrlImagen(),
                platoEntity.getCategoria(),
                platoEntity.getRestauranteId()
        );
    }
}
