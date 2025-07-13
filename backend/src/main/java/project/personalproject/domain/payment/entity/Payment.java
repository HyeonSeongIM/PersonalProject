package project.personalproject.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.global.util.BaseTimeEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String orderId;

    private int amount;

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private PaymentStatus status; // PENDING, SUCCESS, FAILED

    public static Payment from(CreatePaymentCommand request) {
        return Payment.builder()
                .memberId(request.memberId())
                .orderId(request.orderId())
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .build();
    }

}
