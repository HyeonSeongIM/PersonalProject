package project.personalproject.domain.payment.dto.response;

public record PgWebhookResponse(
        String orderId,
        String status
) {
}
