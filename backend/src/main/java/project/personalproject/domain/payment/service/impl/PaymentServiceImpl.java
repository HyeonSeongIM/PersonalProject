package project.personalproject.domain.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.entity.Payment;
import project.personalproject.domain.payment.repository.PaymentRepository;
import project.personalproject.domain.payment.service.PaymentService;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(CreatePaymentCommand command) {
        Payment payment = Payment.from(command);

        return paymentRepository.save(payment);
    }
}
