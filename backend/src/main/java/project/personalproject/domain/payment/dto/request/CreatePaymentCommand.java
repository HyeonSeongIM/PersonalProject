package project.personalproject.domain.payment.dto.request;

public record CreatePaymentCommand(
        Long memberId,
        String orderId,
        int amount
) {


}
