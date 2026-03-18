package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.repository;

import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRestauranteRepository  extends JpaRepository<RestauranteEntity, Long> {
}
