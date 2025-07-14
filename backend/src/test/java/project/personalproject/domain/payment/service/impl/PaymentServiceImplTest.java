package project.personalproject.domain.payment.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.dto.request.PgRequest;
import project.personalproject.domain.payment.dto.request.PgWebhookRequest;
import project.personalproject.domain.payment.dto.response.PgResponse;
import project.personalproject.domain.payment.entity.Payment;
import project.personalproject.domain.payment.entity.PaymentStatus;
import project.personalproject.domain.payment.exception.PgException;
import project.personalproject.domain.payment.repository.PaymentRepository;
import project.personalproject.domain.payment.service.PgClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PgClient pgClient;

    @Test
    void 사용자_결제_요청_후_상태_변환() {
        // given
        CreatePaymentCommand paymentCommand = new CreatePaymentCommand(123L, "test123", 10000);
        Payment payment = Payment.from(paymentCommand);

        when(paymentRepository.save(any())).thenReturn(payment); // 동작 미리 저장

        // when
        Payment result = paymentService.createPayment(paymentCommand);

        // then
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(10000, result.getAmount());
        verify(paymentRepository, times(1)).save(any()); // 몇번 저장되었는지 확인
    }

    @Test
    void PG_결제_요청() {
        // given
        CreatePaymentCommand paymentCommand = new CreatePaymentCommand(123L, "ORDER123", 10000);
        PgRequest pgRequest = new PgRequest(paymentCommand.orderId(), paymentCommand.amount());
        PgResponse pgResponse = new PgResponse("PG_ORDER_456", "READY");

        when(pgClient.requestPayment(any())).thenReturn(pgResponse);
        // save 처럼 인가를 가공하지 않는 곳에서 유뤼
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0)); // 넘어온 걸 그대로 반환

        // when
        Payment result = paymentService.createPayment(paymentCommand);

        // then
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(10000, result.getAmount());
        verify(pgClient, times(1)).requestPayment(pgRequest);
        verify(paymentRepository, times(1)).save(any());

    }

    @Test
    void 결제_성공_webhook_수신시_상태_SUCCESS_변경() {
        // given
        PgWebhookRequest pgWebhookRequest = new PgWebhookRequest("PG_ORDER_456", "SUCCESS");
        CreatePaymentCommand paymentCommand = new CreatePaymentCommand(123L, "ORDER123", 10000);
        Payment payment = Payment.from(paymentCommand);

        when(paymentRepository.findByOrderId("PG_ORDER_456")).thenReturn(payment);

        // when
        Payment result = paymentService.updatePayment(pgWebhookRequest);

        // then
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
    }

    @Test
    void 결제_실패_webhook_수신시_상태_FAILED_변경() {
        // given
        PgWebhookRequest pgWebhookRequest = new PgWebhookRequest("PG_ORDER_456", "FAILED");
        CreatePaymentCommand paymentCommand = new CreatePaymentCommand(123L, "ORDER123", 10000);
        Payment payment = Payment.from(paymentCommand);
        when(paymentRepository.findByOrderId("PG_ORDER_456")).thenReturn(payment);

        // when
        Payment result = paymentService.updatePayment(pgWebhookRequest);

        // then
        assertEquals(PaymentStatus.FAILED, result.getStatus());
    }

}