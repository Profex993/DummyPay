package com.egersoft.dummypay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NewPaymentSessionDTO {

    @Schema(description = "Amount in the smallest currency unit", example = "1099", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "amount is required")
    private Integer amount;

    @Schema(description = "ISO currency code", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "currency is required")
    private String currency;

    @Schema(description = "Merchant name", example = "acme-store", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "merchant is required")
    private String merchant;

    @Schema(description = "Webhook URL for payment status updates", example = "https://merchant.example.com/webhooks/payment-update", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "updateWebhook is required")
    private String updateWebhook;
}