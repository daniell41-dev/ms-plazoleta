package com.plazoleta.ms_plazoleta.application.usecase;

import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioDelRestauranteException;
import com.plazoleta.ms_plazoleta.domain.exception.PlatoNoEncontradoException;
import com.plazoleta.ms_plazoleta.domain.exception.RestauranteNoEncontradoException;
import com.plazoleta.ms_plazoleta.domain.model.Plato;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.in.IPlatoServicePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IPlatoPersistencePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;

import java.util.Optional;

public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;

    public PlatoUseCase(IPlatoPersistencePort platoPersistencePort,
                        IRestaurantePersistencePort restaurantePersistencePort) {
        this.platoPersistencePort = platoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
    }

    @Override
    public void guardarPlato(Plato plato, Long propietarioId) {

        Optional<Restaurante> restauranteOpt = restaurantePersistencePort.buscarRestaurantePorId(plato.getRestauranteId());
        if (restauranteOpt.isEmpty()) {
            throw new RestauranteNoEncontradoException("Restaurante no encontrado");
        }

        Restaurante restaurante = restauranteOpt.get();
        if (!restaurante.getPropietarioId().equals(propietarioId)) {
            throw new NoPropietarioDelRestauranteException("No es el propietario de este restaurante");
        }

        if (plato.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void actualizarPlato(Long id, Integer precio, String descripcion, Long propietarioId) {
        Optional<Plato> platoOpt = platoPersistencePort.buscarPlatoPorId(id);
        if (platoOpt.isEmpty()) {
            throw new PlatoNoEncontradoException("Plato no encontrado");
        }

        Plato plato = platoOpt.get();

        Optional<Restaurante> restauranteOpt = restaurantePersistencePort.buscarRestaurantePorId(plato.getRestauranteId());
        if (restauranteOpt.isEmpty()) {
            throw new RestauranteNoEncontradoException("Restaurante no encontrado");
        }

        Restaurante restaurante = restauranteOpt.get();
        if (!restaurante.getPropietarioId().equals(propietarioId)) {
            throw new NoPropietarioDelRestauranteException("No es el propietario de este restaurante");
        }

        if (precio != null) {
            if (precio <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a 0");
            }
            plato.setPrecio(precio);
        }

        if (descripcion != null) {
            plato.setDescripcion(descripcion);
        }

        platoPersistencePort.guardarPlato(plato);

    }
}
