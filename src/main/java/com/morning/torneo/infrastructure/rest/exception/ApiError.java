package com.morning.torneo.infrastructure.rest.exception;

import java.time.LocalDateTime;

import com.morning.torneo.application.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String correlationId;
    private String path;

    public ApiError(LocalDateTime timestamp, int status, String error,
                    String message, String correlationId, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.correlationId = correlationId;
        this.path = path;
    }

    public static ApiError from(HttpServletRequest request, int status, String error, String message) {
        String correlationId = request.getAttribute("correlationId") != null
                ? request.getAttribute("correlationId").toString()
                : "N/A";
        return new ApiError(
                LocalDateTime.now(),
                status,
                error,
                message,
                correlationId,
                request.getRequestURI()
        );
    }

    public ErrorResponse toResponse() {
        return new ErrorResponse(timestamp, status, error, message, correlationId, path);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
