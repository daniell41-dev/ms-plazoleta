package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.repository;

import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlatoRepository extends JpaRepository<PlatoEntity, Long> {
}
