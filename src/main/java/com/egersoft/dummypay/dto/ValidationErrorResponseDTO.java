package com.egersoft.dummypay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Schema(description = "Validation errors keyed by field name")
public class ValidationErrorResponseDTO {
    @Schema(description = "Map of field names to validation error messages", example = "{\"updateWebhook\":\"updateWebhook is required\"}")
    private Map<String, String> errors;
}