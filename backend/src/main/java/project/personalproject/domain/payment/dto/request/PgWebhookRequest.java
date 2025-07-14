package project.personalproject.domain.payment.dto.request;

public record PgWebhookRequest(
        String orderId,
        String status
) {
}
