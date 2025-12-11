package com.egersoft.dummypay.controller;

import com.egersoft.dummypay.dto.PaymentSessionUserViewDTO;
import com.egersoft.dummypay.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentPageController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Payment page",
            description = "Renders the payment page for the given paymentId and attaches PaymentSessionUserViewDTO to the model.\nLets user preform payment or canceling.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment page (HTML) rendered successfully; model contains PaymentSessionUserViewDTO.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentSessionUserViewDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Payment not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{paymentId}")
    public String paymentPage(@PathVariable Long paymentId, Model model) {
        PaymentSessionUserViewDTO payment = paymentService.getUserPaymentSessionViewDTO(paymentId);
        model.addAttribute("payment", payment);
        return "payment";
    }
}