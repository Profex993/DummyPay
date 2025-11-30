package com.egersoft.dummypay.service;

import com.egersoft.dummypay.dto.NewPaymentSessionDTO;
import com.egersoft.dummypay.dto.PaymentSessionUserViewDTO;
import com.egersoft.dummypay.exception.DatabaseInstanceNotFoundException;
import com.egersoft.dummypay.exception.InvalidPaymentStatusException;
import com.egersoft.dummypay.model.PaymentSession;
import com.egersoft.dummypay.model.PaymentStatus;
import com.egersoft.dummypay.repository.PaymentSessionRepository;
import com.egersoft.dummypay.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentSessionRepository paymentSessionRepository;
    private final IdGenerator idGenerator;

    public long createNewPaymentSession(NewPaymentSessionDTO dto) throws Exception {
        long id = idGenerator.generateId();

        PaymentSession payment = new PaymentSession();
        payment.setId(id);
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        payment.setMerchantName(dto.getMerchant());
        payment.setStatus(PaymentStatus.OPEN);
        payment.setUpdateWebhook(dto.getUpdateWebhook());
        payment.setCreatedAt(Instant.now());
        payment.setClosedAt(null);

        paymentSessionRepository.save(payment);

        return id;
    }

    public PaymentSessionUserViewDTO getUserPaymentSessionViewDTO(long paymentId) {
        PaymentSession ps = paymentSessionRepository.findById(paymentId)
                .orElseThrow(() -> new DatabaseInstanceNotFoundException("Payment session not found: " + paymentId));

        return PaymentSessionUserViewDTO.builder()
                .amount(ps.getAmount())
                .currency(ps.getCurrency())
                .status(ps.getStatus())
                .merchantName(ps.getMerchantName())
                .createdAt(ps.getCreatedAt())
                .closedAt(ps.getClosedAt())
                .build();
    }

    public void setPaymentStatusPaid(long id) {
        PaymentSession session = paymentSessionRepository.findById(id).orElseThrow(() -> new DatabaseInstanceNotFoundException("session not found"));

        if (session.getStatus() == PaymentStatus.CLOSED || session.getStatus() == PaymentStatus.PAID) {
            throw new InvalidPaymentStatusException("can not pay closed payment");
        }

        session.setStatus(PaymentStatus.PAID);
        session.setClosedAt(Instant.now());
        paymentSessionRepository.save(session);
    }

    public void setPaymentStatusClosed(long id) {
        PaymentSession session = paymentSessionRepository.findById(id).orElseThrow(() -> new DatabaseInstanceNotFoundException("session not found"));
        session.setStatus(PaymentStatus.CLOSED);
        session.setClosedAt(Instant.now());
        paymentSessionRepository.save(session);
    }
}
