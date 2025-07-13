package project.personalproject.domain.payment.service;

import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.entity.Payment;

public interface PaymentService {

    // 결제 로직 생성
    Payment createPayment(CreatePaymentCommand command);
}
