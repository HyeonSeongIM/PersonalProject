package project.personalproject.domain.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.dto.request.PgRequest;
import project.personalproject.domain.payment.dto.request.PgWebhookRequest;
import project.personalproject.domain.payment.entity.Payment;
import project.personalproject.domain.payment.entity.PaymentStatus;
import project.personalproject.domain.payment.exception.PaymentException;
import project.personalproject.domain.payment.exception.PgException;
import project.personalproject.domain.payment.repository.PaymentRepository;
import project.personalproject.domain.payment.service.PaymentService;
import project.personalproject.domain.payment.service.PgClient;
import project.personalproject.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PgClient pgClient;

    @Override
    public Payment createPayment(CreatePaymentCommand command) {
        PgRequest pgRequest = new PgRequest(command.orderId(), command.amount());

        Payment payment = Payment.from(command);

        try {
            pgClient.requestPayment(pgRequest);
        } catch (PgException e) {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(PgWebhookRequest request) {
        Payment payment = paymentRepository.findByOrderId(request.orderId());

        if (payment == null) {
            throw new PaymentException(ErrorCode.NOT_MATCH_USER);
        }

        payment.setStatus(PaymentStatus.valueOf(request.status()));

        return payment;
    }
}
