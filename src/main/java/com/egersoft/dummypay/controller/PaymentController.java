package com.egersoft.dummypay.controller;

import com.egersoft.dummypay.dto.*;
import com.egersoft.dummypay.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Create a new payment session",
            description = "Creates a new payment session. Required fields: amount (number), currency (string), merchant (string), updateWebhook (string).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "New payment session payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewPaymentSessionDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment session created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreatedSessionResponseDTO.class,
                                            description = "Response containing the created session id",
                                            example = "{\"sessionId\": 123}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error in the request payload",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class,
                                            description = "Validation errors keyed by field",
                                            example = "{\"errors\": {\"updateWebhook\": \"updateWebhook is required\"}}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class,
                                            example = "{\"status\":500,\"message\":\"creating session failed\",\"path\":\"/api/payment\",\"errorCode\":\"GEN_001\",\"timestamp\":\"2025-01-01T12:00:00Z\"}")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody NewPaymentSessionDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ValidationErrorResponseDTO(fieldErrors));
        }

        try {
            long id = paymentService.createNewPaymentSession(dto);
            return ResponseEntity.ok(new CreatedSessionResponseDTO(id));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "creating session failed",
                    "/api/payment",
                    "GEN_001"
            );

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }

    @Operation(
            summary = "Get payment session",
            description = "Returns the payment session view for the user containing amount, currency, status, merchantName and timestamps.",
            parameters = {
                    @Parameter(name = "paymentId", description = "Identifier of the payment session", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment session retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentSessionUserViewDTO.class,
                                            description = "Payment session user view",
                                            example = "{\"amount\":1099,\"currency\":\"USD\",\"status\":\"OPEN\",\"merchantName\":\"acme-store\",\"createdAt\":\"2025-01-01T12:00:00Z\",\"closedAt\":null}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Payment not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentSession(@PathVariable long paymentId) {
        PaymentSessionUserViewDTO dto = paymentService.getUserPaymentSessionViewDTO(paymentId);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(
            summary = "Mark payment as paid",
            description = "Set a payment's status to 'paid'.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment marked as paid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class, example = "{\"status\":\"ok\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Payment not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request or invalid payment state",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PutMapping("/{paymentId}/pay")
    public ResponseEntity<?> markAsPaid(@PathVariable long paymentId) {
        paymentService.setPaymentStatusPaid(paymentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("status", "ok"));
    }

    @Operation(
            summary = "Mark payment as closed",
            description = "Set a payment's status to 'closed'",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment marked as closed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class, example = "{\"status\":\"ok\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Payment not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PutMapping("/{paymentId}/close")
    public ResponseEntity<?> markAsClosed(@PathVariable long paymentId) {
        paymentService.setPaymentStatusClosed(paymentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("status", "ok"));
    }
}