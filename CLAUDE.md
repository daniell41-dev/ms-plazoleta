# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Proyecto

Microservicio `ms-plazoleta` — parte de un sistema de plazoleta de comidas. Construido con Java 21, Spring Boot 4, PostgreSQL y Lombok. Sigue **arquitectura hexagonal** (puertos y adaptadores).

El proyecto de referencia es `ms-usuario` en `D:/practica plazoleta/ms-usuario plazoleta/`.

## Comandos

```bash
# Compilar
./gradlew build

# Ejecutar
./gradlew bootRun

# Tests
./gradlew test

# Un solo test
./gradlew test --tests "ms_plazoleta.ms_plazoleta.NombreDelTest"
```

## Arquitectura hexagonal

```
domain/
  model/         → modelos de dominio puros (sin dependencias externas, sin Lombok)
  ports/
    in/          → interfaces que expone el dominio (IRestauranteServicePort)
    out/         → interfaces que el dominio necesita (IRestaurantePersistencePort)

application/
  usecase/       → implementan los puertos in, contienen lógica de negocio

infrastructure/
  config/        → BeanConfiguration: conecta puertos con implementaciones (sin @Service en casos de uso)
  input/rest/
    dto/         → objetos de entrada con validaciones Jakarta (@NotBlank, @Pattern, etc.)
    mapper/      → convierte DTO → modelo de dominio
    *RestController.java
  output/persistence/
    entity/      → entidades JPA (@Entity, usa Lombok)
    mapper/      → interfaz + implementación: modelo de dominio ↔ entidad JPA
    repository/  → extiende JpaRepository
    adapter/     → implementa el puerto out usando repository + mapper
```

## Reglas del proyecto

- El **dominio** no tiene dependencias externas: sin Lombok, sin Spring, sin JPA.
- Los **casos de uso** no tienen `@Service` — se registran como beans en `BeanConfiguration`.
- Los **mappers de request** son `@Component` concretos (no interfaces).
- Los **mappers de entidad** son interfaz + implementación `@Component`.
- Validaciones de **formato** van en el DTO; validaciones de **negocio** van en el caso de uso.

## Comportamiento esperado de Claude

El usuario está **practicando y aprendiendo**. Crear archivos de **uno en uno**, explicando qué hace cada uno y por qué existe, luego esperar confirmación antes de continuar con el siguiente.
