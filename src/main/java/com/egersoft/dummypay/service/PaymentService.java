package com.egersoft.dummypay.service;

import com.egersoft.dummypay.dto.NewPaymentSessionDTO;
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

    public long createNewPaymentSession(NewPaymentSessionDTO dto) {
        try {
            long id = idGenerator.generateId();

            PaymentSession payment = new PaymentSession();
            payment.setId(id);
            payment.setAmount(dto.getAmount());
            payment.setCurrency(dto.getCurrency());
            payment.setMerchantName(dto.getMerchant());
            payment.setStatus(PaymentStatus.OPEN);
            payment.setSuccessWebhook(dto.getUpdateWebhook());
            payment.setCreatedAt(Instant.now());
            payment.setClosedAt(null);

            paymentSessionRepository.save(payment);

            return id;
        } catch (Exception e) {
            throw new RuntimeException("failed to create payment session");
        }
    }
}
