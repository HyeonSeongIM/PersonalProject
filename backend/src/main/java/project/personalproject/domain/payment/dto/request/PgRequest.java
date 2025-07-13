package project.personalproject.domain.payment.dto.request;

public record PgRequest(
        String orderId,
        int amount
) {
}
