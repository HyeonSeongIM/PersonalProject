package project.personalproject.domain.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.dto.request.PgRequest;
import project.personalproject.domain.payment.entity.Payment;
import project.personalproject.domain.payment.repository.PaymentRepository;
import project.personalproject.domain.payment.service.PaymentService;
import project.personalproject.domain.payment.service.PgClient;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PgClient pgClient;

    @Override
    public Payment createPayment(CreatePaymentCommand command) {
        PgRequest pgRequest = new PgRequest(command.orderId(), command.amount());

        pgClient.requestPayment(pgRequest);

        Payment payment = Payment.from(command);

        return paymentRepository.save(payment);
    }
}
