package com.egersoft.dummypay.dto;

import com.egersoft.dummypay.model.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload sent to the merchant updateWebhook when a payment's status changes.")
public class PaymentWebhookResponseDTO {

    @Schema(description = "Payment session identifier", example = "123")
    private long sessionId;

    @Schema(
            description = "New status of the payment session",
            example = "PAID",
            allowableValues = {"OPEN", "PAID", "CLOSED"}
    )
    private PaymentStatus status;
}