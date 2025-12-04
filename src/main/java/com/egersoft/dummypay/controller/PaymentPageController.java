package com.egersoft.dummypay.controller;

import com.egersoft.dummypay.dto.PaymentSessionUserViewDTO;
import com.egersoft.dummypay.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentPageController {

    private final PaymentService paymentService;

    @GetMapping("/{paymentId}")
    public String paymentPage(@PathVariable Long paymentId, Model model) {
        PaymentSessionUserViewDTO payment = paymentService.getUserPaymentSessionViewDTO(paymentId);
        model.addAttribute("payment", payment);
        return "payment";
    }
}
