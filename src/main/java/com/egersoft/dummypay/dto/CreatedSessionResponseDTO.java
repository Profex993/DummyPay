package com.egersoft.dummypay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response returned when a payment session is created")
public class CreatedSessionResponseDTO {
    @Schema(description = "Identifier of the created session", example = "123")
    private long sessionId;
}