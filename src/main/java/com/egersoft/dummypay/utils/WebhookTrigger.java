package com.egersoft.dummypay.utils;

import com.egersoft.dummypay.dto.PaymentWebhookResponseDTO;
import com.egersoft.dummypay.exception.WebhookBadResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class WebhookTrigger {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void triggerWebhook(PaymentWebhookResponseDTO event, String callbackUrl) {
        try {
            String body = objectMapper.writeValueAsString(event);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(callbackUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            if (status < 200 || status >= 300) {
                throw new WebhookBadResponseException("Webhook responded with status: " + status);
            }
        } catch (Exception e) {
            throw new WebhookBadResponseException("Webhook failed");
        }
    }
}