package com.egersoft.dummypay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "payment_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSession {
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "marchant_name", nullable = false)
    private String merchantName;

    @Column(name = "success_webhook", nullable = false)
    private String successWebhook;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "closed_at")
    private Instant closedAt;

}
