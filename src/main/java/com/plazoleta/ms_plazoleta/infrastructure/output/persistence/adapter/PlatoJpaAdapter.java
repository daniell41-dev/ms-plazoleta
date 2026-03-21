package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.adapter;

import com.plazoleta.ms_plazoleta.domain.model.Plato;
import com.plazoleta.ms_plazoleta.domain.ports.out.IPlatoPersistencePort;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper.IPlatoEntityMapper;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.repository.IPlatoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlatoJpaAdapter implements IPlatoPersistencePort {

    private final IPlatoRepository platoRepository;
    private final IPlatoEntityMapper platoEntityMapper;

    public PlatoJpaAdapter(IPlatoRepository platoRepository, IPlatoEntityMapper platoEntityMapper) {
        this.platoRepository = platoRepository;
        this.platoEntityMapper = platoEntityMapper;
    }

    @Override
    public void guardarPlato(Plato plato) {
        platoRepository.save(platoEntityMapper.toEntity(plato));
    }

    @Override
    public Optional<Plato> buscarPlatoPorId(Long id) {
        return platoRepository.findById(id)
                .map(platoEntityMapper::toPlato);
    }
}
