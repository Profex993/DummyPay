# DummyPay

DummyPay is a lightweight, developer-focused payment gateway stub built with Spring Boot. It’s designed to be simple to add to your application for local development, demos, and integration testing before you implement a real payment provider.

> Non‑production use only. DummyPay is for testing and demonstration purposes—not for processing real payments.

## Key Features

- Simple to integrate: drop-in Spring Boot application with a clean REST API.
- Fast demo and prototyping: simulate payment flows.
- OpenAPI documentation: the API is described and documented via OpenAPI/Swagger.
- Insomnia collection: a file is included for Insomnia to make API testing quick.
- Mock server: a minimal mock server is included to receive and log webhook callbacks for local testing.

## Implementation Steps
1. Create relational database instance of your choice

2. Download the JAR build

3. Configure `application.properties`
    - Adjust the application settings to match your environment and needs (ports, database, etc.).

4. Use the APIs
    - Create a payment session, then call the status APIs to simulate payment outcomes.
    - The “set payment status” APIs trigger a webhook using the URL you provided when creating the session.

## Mock Server

A minimal mock server is included to help you test webhooks locally. It accepts POST requests (e.g., from DummyPay) and logs the payload for inspection. Use it to verify what your application would receive from the webhook before integrating a real gateway.

- Purpose:
    - Receive webhook callbacks at a local endpoint.
    - Log and inspect payloads during development.
- Typical usage:
    - Start the mock server locally.
    - Configure `updateWebhook` in your `POST /api/payment` request to point to the mock server’s `/update` endpoint.
    - Trigger payment status changes (`/pay`, `/close`) and observe received payloads.

## OpenAPI and Insomnia

- OpenAPI/Swagger: The API is annotated with OpenAPI; Swagger UI exposes endpoint docs for interactive exploration.
- Insomnia: An Insomnia collection file is included to quickly import and start testing the API.

## Typical Flow

1. Create a payment session with your `updateWebhook`.
2. Retrieve the session to display in your UI.
3. Simulate outcomes:
    - Call `/pay` to mark the session as `PAID` and trigger your webhook.
    - Call `/close` to mark the session as `CLOSED` and trigger your webhook.
4. Handle the webhook in your application and update its state accordingly.

## Tech Stack

- Java (Spring Boot)
- Python