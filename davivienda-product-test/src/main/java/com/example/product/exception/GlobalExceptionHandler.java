package com.example.product.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de validación (Body inválido) u ptro tipo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));

        ErrorResponse response = ErrorResponse.builder()
                .message("Los datos enviados no son válidos. Revisa los campos.")
                .code("VALIDATION_ERROR")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .errors(fieldErrors)
                .technicalMessage(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // Violaciones de integridad
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String technical = ex.getMostSpecificCause() != null ?
                ex.getMostSpecificCause().getMessage() : ex.getMessage();

        String message = "Los datos proporcionados no cumplen las restricciones del sistema.";
        String code = "DATA_INTEGRITY_ERROR";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Caso específico de nombre duplicado
        if (technical != null && technical.contains("UNIQUE")
                || technical.contains("PRIMARY KEY")
                || technical.contains("CONSTRAINT_INDEX_F")) {

            message = "Ya existe un producto registrado con ese nombre. El nombre debe ser único.";
            code = "PRODUCT_NAME_DUPLICATE";
            status = HttpStatus.CONFLICT; // 409
        }

        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .code(code)
                .status(status.value())
                .path(request.getRequestURI())
                .technicalMessage(technical)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Map<String, String> violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),  // ej: "getById.id"
                        v -> v.getMessage(),
                        (m1, m2) -> m1 // en caso de keys repetidas
                ));

        String message = "Uno o más parámetros de la petición no son válidos.";
        String code = "CONSTRAINT_VIOLATION";


        boolean idProblem = violations.keySet().stream().anyMatch(k -> k.endsWith(".id"));
        if (idProblem) {
            message = "El id del producto es obligatorio y debe ser un número entero positivo.";
            code = "PRODUCT_ID_INVALID";
        }

        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .code(code)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .errors(violations)
                .technicalMessage(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralErrors(
            Exception ex,
            HttpServletRequest request) {

        String technical = ex.getMessage();
        String path = request.getRequestURI();

        // URL mal formada /api/products/
        if (technical != null && technical.contains("No static resource api/products")) {

            ErrorResponse response = ErrorResponse.builder()
                    .message("La URL solicitada no es válida. " +
                            "Para listar productos usa /api/products y " +
                            "para operar por id usa /api/products/{id}, por ejemplo /api/products/1.")
                    .code("ENDPOINT_NOT_FOUND")
                    .status(HttpStatus.NOT_FOUND.value())
                    .path(path)
                    .technicalMessage(technical)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Errores desconocidos
        ErrorResponse response = ErrorResponse.builder()
                .message("Ocurrió un error inesperado al procesar la solicitud. Inténtalo de nuevo.")
                .code("INTERNAL_SERVER_ERROR")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(path)
                .technicalMessage(technical)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
