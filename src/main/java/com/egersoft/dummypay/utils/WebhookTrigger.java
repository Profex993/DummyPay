package com.egersoft.dummypay.utils;

import com.egersoft.dummypay.exception.WebhookBadResponseException;
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

    public void triggerWebhook(String body, String callbackUrl) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(callbackUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new WebhookBadResponseException("Webhook responded with status: " + response.statusCode());
            }

        } catch (Exception e) {
            throw new WebhookBadResponseException("Webhook failed");
        }
    }
}
