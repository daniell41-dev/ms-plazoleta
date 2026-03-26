package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper.IRestauranteEntityMapper;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.repository.IRestauranteRepository;
import org.springframework.stereotype.Component;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestauranteJpaAdapter implements IRestaurantePersistencePort {

    private final IRestauranteRepository restauranteRepository;
    private final IRestauranteEntityMapper restauranteEntityMapper;

    @Override
    public void guardarRestaurante(Restaurante restaurante) {
        restauranteRepository.save(restauranteEntityMapper.toEntity(restaurante));
    }

    @Override
    public Optional<Restaurante> buscarRestaurantePorId(Long idRestaurante) {
        return restauranteRepository.findById(idRestaurante)
                .map(restauranteEntityMapper::toRestaurante);
    }

    @Override
    public List<Restaurante> listarRestaurantes(int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        return restauranteRepository.findAllByOrderByNombreAsc(pageable)
                .stream()
                .map(restauranteEntityMapper::toRestaurante)
                .toList();
    }
}
