package com.egersoft.dummypay.exception;

import com.egersoft.dummypay.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request, String errorCode) {
        ErrorResponse response = new ErrorResponse(status.value(), message, request.getRequestURI(), errorCode);
        return new ResponseEntity<>(response, status);
    }

    // --- VALIDATION ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.debug("Validation failed: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", request, "REQ_001");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
        logger.debug("Missing parameter: {}", ex.getParameterName());
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing required parameter", request, "REQ_002");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logger.debug("Type mismatch for parameter '{}': {}", ex.getName(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid parameter type", request, "REQ_003");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.debug("Malformed JSON request: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", request, "REQ_004");
    }

    // ---PAYMENTS
    @ExceptionHandler(InvalidPaymentStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPaymentsStatus(InvalidPaymentStatusException ex, HttpServletRequest request) {
        logger.debug("Invalid payment status: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid payment status", request, "pay_001");
    }

    // --- DATABASE ---
    @ExceptionHandler(DatabaseInstanceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseInstanceNotFound(DatabaseInstanceNotFoundException ex, HttpServletRequest request) {
        logger.debug("Database instance not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Database instance not found", request, "DB_001");
    }

    @ExceptionHandler(DatabaseInconsistencyException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseInconsistency(DatabaseInconsistencyException ex, HttpServletRequest request) {
        logger.error("Critical database inconsistency: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Critical database error", request, "DB_002");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        logger.error("Database constraint violation: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.BAD_REQUEST, "Database integrity violation", request, "DB_003");
    }

    // --- RESOURCE ---
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException ignore, HttpServletRequest request) {
        logger.debug("No resource found at {}", request.getRequestURI());
        return buildResponse(HttpStatus.NOT_FOUND, "Resource not found", request, "RES_001");
    }

    // --- GENERAL ---
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        logger.error("Runtime error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal runtime error", request, "GEN_001");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", request, "GEN_999");
    }
}
