package com.egersoft.dummypay.dto;

import com.egersoft.dummypay.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class PaymentSessionUserViewDTO {
    private int amount;
    private String currency;
    private PaymentStatus status;
    private String merchantName;
    private Instant createdAt;
    private Instant closedAt;
}