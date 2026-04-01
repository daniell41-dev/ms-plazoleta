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

## Estado actual del proyecto

El paquete base es `com.plazoleta.ms_plazoleta`.

Ya existe toda la estructura base de la HU-2 (Crear restaurante):

- `domain/model/Restaurante.java` — modelo puro con campos: id, nombre, nit, direccion, telefono, urlLogo, propietarioId
- `domain/ports/in/IRestauranteServicePort.java` — método `guardarRestaurante(Restaurante)`
- `domain/ports/out/IRestaurantePersistencePort.java` — método `guardarRestaurante(Restaurante)`
- `application/usecase/RestauranteUseCase.java` — validaciones de NIT, teléfono y nombre implementadas. **Pendiente: validar que propietarioId tenga rol PROPIETARIO**
- `infrastructure/config/BeanConfiguration.java` — registra RestauranteUseCase como bean
- `infrastructure/config/SecurityConfig.java` — seguridad desactivada temporalmente
- `infrastructure/input/rest/dto/RestauranteRequestDto.java` — DTO de entrada
- `infrastructure/input/rest/mapper/RestauranteRequestMapper.java` — mapper DTO → dominio
- `infrastructure/input/rest/RestauranteRestController.java` — `POST /restaurantes` → HTTP 201
- `infrastructure/output/persistence/entity/RestauranteEntity.java` — entidad JPA
- `infrastructure/output/persistence/mapper/IRestauranteEntityMapper.java` + `RestauranteEntityMapper.java`
- `infrastructure/output/persistence/repository/IRestauranteRepository.java`
- `infrastructure/output/persistence/adapter/RestauranteJpaAdapter.java`

## Tarea pendiente: integrar validación de propietario vía Feign Client

`ms-usuario` corre en `http://localhost:8080` y expone:
```
GET /usuarios/{id}/rol → { "rol": "PROPIETARIO" }
```

Antes de guardar un restaurante, el caso de uso debe validar que `propietarioId` corresponda a un usuario con rol `PROPIETARIO`. Si no lo es → lanzar excepción de dominio.

### Archivos a crear (en orden):

1. **`build.gradle`** — agregar dependencia de Feign:
   ```groovy
   implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
   ```
   También requiere agregar el BOM de Spring Cloud en `dependencyManagement`.

2. **`Application.java`** — agregar `@EnableFeignClients` a la clase principal

3. **`IUsuarioClient.java`** — interfaz Feign en `infrastructure/output/feignclient/`:
   ```java
   @FeignClient(name = "ms-usuario", url = "http://localhost:8080")
   public interface IUsuarioClient {
       @GetMapping("/usuarios/{id}/rol")
       UsuarioRolResponseDto obtenerRol(@PathVariable Long id);
   }
   ```

4. **`UsuarioRolResponseDto.java`** — DTO de respuesta en `infrastructure/output/feignclient/dto/`:
   - Campo `String rol` con getter

5. **`IUsuarioServicePort.java`** — puerto OUT del dominio en `domain/ports/out/`:
   - Método `String obtenerRolUsuario(Long id)`
   - Abstrae la llamada HTTP: el dominio no sabe que hay Feign por debajo

6. **`UsuarioFeignAdapter.java`** — en `infrastructure/output/feignclient/`:
   - Implementa `IUsuarioServicePort`
   - Llama a `IUsuarioClient.obtenerRol(id)` y devuelve `response.getRol()`

7. **`BeanConfiguration.java`** — actualizar para inyectar `IUsuarioServicePort` en `RestauranteUseCase`

8. **`RestauranteUseCase.java`** — agregar validación:
   - Recibir `IUsuarioServicePort` por constructor
   - Llamar `usuarioServicePort.obtenerRolUsuario(restaurante.getPropietarioId())`
   - Si el rol no es `"PROPIETARIO"` → lanzar excepción de dominio `NoPropietarioException`

9. **`NoPropietarioException.java`** — excepción de dominio en `domain/exception/`

## Reglas del proyecto

- El **dominio** no tiene dependencias externas: sin Lombok, sin Spring, sin JPA.
- Los **casos de uso** no tienen `@Service` — se registran como beans en `BeanConfiguration`.
- Los **mappers de request** son `@Component` concretos (no interfaces).
- Los **mappers de entidad** son interfaz + implementación `@Component`.
- Validaciones de **formato** van en el DTO; validaciones de **negocio** van en el caso de uso.
- Las **excepciones de dominio** van en `domain/exception/` y no extienden clases de Spring.

## Comportamiento esperado de Claude

El usuario está **practicando y aprendiendo**. Guiar de **un archivo a la vez**, explicando qué hace cada uno y por qué existe, luego esperar confirmación antes de continuar con el siguiente. No dar la solución hasta que el usuario la pida explícitamente.

---

## Flujo de ramas y merge strategy

### Contexto multi-microservicio
El sistema tiene DOS microservicios que comparten el mismo contexto general:
- `ms-usuario` (localhost:8080)
- `ms-plazoleta` (localhost:8081)

Las HUs pueden estar en cualquiera de los dos. Ejemplo: h1 está en ms-usuario.

### Orden de merge
Las ramas se numeran `h<N>-<descripcion>`. El merge a `dev` sigue el orden numérico: h2 antes que h3, h3 antes que h4, etc.

### Flujo exacto para integrar una rama a dev

1. Feature branch lista con su HU (ej: `h3-crear-plato`)
2. Pararse en `dev` y sacar rama auxiliar: `git checkout -b h3-crear-plato-aux`
3. Merge de la feature: `git merge h3-crear-plato`
4. Resolver conflictos
5. PR de `h3-crear-plato-aux` → `dev`
6. Repetir para la siguiente HU en orden numérico

### Regla crítica — evitar conflictos entre aux branches
**Cada rama auxiliar solo toca lo que es PROPIO de su HU.**

- Los archivos compartidos (constantes de dominio, build.gradle con JaCoCo, rename de adaptadores) los aplica la PRIMERA aux que llega a dev.
- Las ramas siguientes **no vuelven a tocar** esos archivos ya mergeados — solo agregan lo nuevo de su HU.
- Si una aux necesita algo que aún no llegó a dev (porque la rama anterior no mergeó), se acepta el conflicto puntual y se resuelve manteniendo ambos cambios.

### Por qué este flujo
El reto de aprendizaje exige que cada feature branch tenga SOLO lo que pide la HU. Los fixes de calidad (constantes, tests, JaCoCo, renombrados) se aplican en la rama auxiliar, nunca en la feature directa.
