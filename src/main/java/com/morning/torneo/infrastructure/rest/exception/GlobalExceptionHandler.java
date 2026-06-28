package com.morning.torneo.infrastructure.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import com.morning.torneo.application.dto.ErrorResponse;
import com.morning.torneo.domain.exception.CombateInvalidoException;
import com.morning.torneo.domain.exception.CredencialesInvalidasException;
import com.morning.torneo.domain.exception.DesempateNoPermitidoException;
import com.morning.torneo.domain.exception.EspecieInvalidaException;
import com.morning.torneo.domain.exception.EspecieNoEncontradaException;
import com.morning.torneo.domain.exception.EspecieVsSiMismaException;
import com.morning.torneo.domain.exception.EspecieYaExisteException;
import com.morning.torneo.domain.exception.UsuarioYaExisteException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EspecieInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleEspecieInvalida(EspecieInvalidaException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 400, "Bad Request", "BUSINESS_RULE_VIOLATION", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toResponse());
    }

    @ExceptionHandler(EspecieYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleEspecieYaExiste(EspecieYaExisteException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 409, "Conflict", "CONFLICT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error.toResponse());
    }

    @ExceptionHandler(EspecieNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleEspecieNoEncontrada(EspecieNoEncontradaException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 404, "Not Found", "NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toResponse());
    }

    @ExceptionHandler(EspecieVsSiMismaException.class)
    public ResponseEntity<ErrorResponse> handleEspecieVsSiMisma(EspecieVsSiMismaException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 400, "Bad Request", "BUSINESS_RULE_VIOLATION", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toResponse());
    }

    @ExceptionHandler(DesempateNoPermitidoException.class)
    public ResponseEntity<ErrorResponse> handleDesempateNoPermitido(DesempateNoPermitidoException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 403, "Forbidden", "FORBIDDEN", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.toResponse());
    }

    @ExceptionHandler(CombateInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleCombateInvalido(CombateInvalidoException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 400, "Bad Request", "BUSINESS_RULE_VIOLATION", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toResponse());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredencialesInvalidas(CredencialesInvalidasException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 401, "Unauthorized", "INVALID_CREDENTIALS", "No tienes permisos para realizar esta accion");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error.toResponse());
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioYaExiste(UsuarioYaExisteException ex, HttpServletRequest request) {
        ApiError error = ApiError.from(request, 409, "Conflict", "CONFLICT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error.toResponse());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = buildValidationMessage(ex);
        ApiError error = ApiError.from(request, 400, "Bad Request", "VALIDATION_ERROR", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        String correlationId = request.getAttribute("correlationId") != null
            ? request.getAttribute("correlationId").toString()
            : "N/A";
        logger.error("Error 500 no controlado [correlationId={}]: {}", correlationId, ex.getMessage(), ex);
        ApiError error = ApiError.from(request, 500, "Internal Server Error",
                "INTERNAL_ERROR", "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.toResponse());
    }

    private String buildValidationMessage(MethodArgumentNotValidException ex) {
        if (!ex.getBindingResult().hasFieldErrors()) {
            return "Error de validacion";
        }
        return ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> {
                String fieldName = toSnakeCase(fieldError.getField());
                String message = resolveValidationMessage(fieldError);
                return "El campo '" + fieldName + "' " + message;
            })
            .collect(Collectors.joining("; "));
    }

    private String resolveValidationMessage(FieldError fieldError) {
        String code = fieldError.getCode();
        if ("NotBlank".equals(code)) {
            return "es obligatorio";
        }
        if ("Size".equals(code)) {
            Object[] arguments = fieldError.getArguments();
            Object min = arguments != null && arguments.length > 1 ? arguments[1] : "?";
            Object max = arguments != null && arguments.length > 2 ? arguments[2] : "?";
            return "debe tener entre " + min + " y " + max + " caracteres";
        }
        if ("Min".equals(code)) {
            Object[] arguments = fieldError.getArguments();
            Object value = arguments != null && arguments.length > 1 ? arguments[1] : "?";
            return "debe ser mayor o igual a " + value;
        }
        if ("Max".equals(code)) {
            Object[] arguments = fieldError.getArguments();
            Object value = arguments != null && arguments.length > 1 ? arguments[1] : "?";
            return "debe ser menor o igual a " + value;
        }
        if ("Email".equals(code)) {
            return "debe tener formato de email valido";
        }
        if ("Pattern".equals(code)) {
            return "no tiene el formato esperado";
        }
        return "es invalido";
    }

    private String toSnakeCase(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                if (result.length() > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
