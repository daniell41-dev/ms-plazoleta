package com.plazoleta.ms_plazoleta.infrastructure.config;

import com.plazoleta.ms_plazoleta.application.usecase.RestauranteUseCase;
import com.plazoleta.ms_plazoleta.domain.ports.in.IRestauranteServicePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IRestauranteServicePort restauranteServicePort(
            IRestaurantePersistencePort restaurantePersistencePort) {
        return new RestauranteUseCase(restaurantePersistencePort);
    }
}
