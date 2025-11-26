package com.egersoft.dummypay.controller;

import com.egersoft.dummypay.dto.NewPaymentSessionDTO;
import com.egersoft.dummypay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody NewPaymentSessionDTO dto) {
        long id = paymentService.createNewPaymentSession(dto);
        return ResponseEntity.ok(id);
    }
}
