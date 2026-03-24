package com.plazoleta.ms_plazoleta.infrastructure.input.rest;

import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioDelRestauranteException;
import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioException;
import com.plazoleta.ms_plazoleta.domain.exception.PlatoNoEncontradoException;
import com.plazoleta.ms_plazoleta.domain.exception.RestauranteNoEncontradoException;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.ErrorResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Campos requeridos faltantes o formato inválido (anotaciones @NotNull, @NotBlank, @Pattern, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(400, mensaje));
    }

    // Validaciones de negocio: NIT inválido, teléfono inválido, precio <= 0, etc.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(400, ex.getMessage()));
    }

    // El propietarioId enviado no tiene rol PROPIETARIO en ms-usuario
    @ExceptionHandler(NoPropietarioException.class)
    public ResponseEntity<ErrorResponseDto> handleNoPropietario(NoPropietarioException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(403, ex.getMessage()));
    }

    // El propietario autenticado no es dueño de ese restaurante
    @ExceptionHandler(NoPropietarioDelRestauranteException.class)
    public ResponseEntity<ErrorResponseDto> handleNoPropietarioDelRestaurante(NoPropietarioDelRestauranteException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(403, ex.getMessage()));
    }

    @ExceptionHandler(PlatoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDto> handlePlatoNoEncontrado(PlatoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, ex.getMessage()));
    }

    @ExceptionHandler(RestauranteNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDto> handleRestauranteNoEncontrado(RestauranteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, ex.getMessage()));
    }

    // NIT duplicado u otra violación de restricción única en la base de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicado(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(409, "Ya existe un registro con esos datos (posible NIT duplicado)"));
    }

    // Fallback para cualquier error no contemplado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(500, "Error interno del servidor"));
    }
}
