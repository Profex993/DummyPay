package com.egersoft.dummypay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class NewPaymentSessionDTO {
    private int amount;
    private String currency, merchant, updateWebhook;
    private Instant createdAt, closedAt;
}
