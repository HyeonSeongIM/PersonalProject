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
import project.personalproject.domain.payment.exception.PaymentException;
import project.personalproject.domain.payment.exception.PgException;
import project.personalproject.domain.payment.repository.PaymentRepository;
import project.personalproject.domain.payment.service.PgClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private SettlementServiceImpl settlementService;

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

    @Test
    void PG_요청_실패시_결제_상태_FAILED_저장() {
        // given
        CreatePaymentCommand paymentCommand = new CreatePaymentCommand(123L, "ORDER123", 10000);
        PgRequest pgRequest = new PgRequest(paymentCommand.orderId(), paymentCommand.amount());

        when(pgClient.requestPayment(any())).thenThrow(PgException.class);
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Payment result = paymentService.createPayment(paymentCommand);

        //then
        assertEquals(PaymentStatus.FAILED, result.getStatus());
        verify(pgClient, times(1)).requestPayment(eq(pgRequest)); // 동일성이 아닌 동등성비교
    }

    @Test
    void 결제_실패_webhook_수신시_상태_FAIL_변경() {
        // given
        CreatePaymentCommand paymentCommand = new CreatePaymentCommand(123L, "ORDER123", 10000);
        Payment payment = Payment.from(paymentCommand);
        PgWebhookRequest pgWebhookRequest = new PgWebhookRequest("ORDER123", "FAILED");

        when(paymentRepository.findByOrderId("ORDER123")).thenReturn(payment);

        // when
        Payment updated = paymentService.updatePayment(pgWebhookRequest);

        // then
        assertEquals(PaymentStatus.FAILED, updated.getStatus());


    }

    @Test
    void 존재하지_않는_orderId_webhook_수신시_예외_발생() {
        // given
        PgWebhookRequest pgWebhookRequest = new PgWebhookRequest("WRONG_ORDER123", "SUCCESS");

//        // 실제 테스트하고자 하는 로직은 mock 하면 안됩니다.
//        // 저장이 목적이 아닌 테스트 코드는 save를 할 필요가 없습니다.
//        when(paymentRepository.save(any())).thenReturn(payment);
//        when(paymentService.updatePayment(pgWebhookRequest)).thenReturn(payment);
        when(paymentRepository.findByOrderId("WRONG_ORDER123")).thenReturn(null);

        // when & then
        assertThrows(PaymentException.class, () -> paymentService.updatePayment(pgWebhookRequest));


    }

    @Test
    void 결제_성공시_정산_요청_수행() {
        // given
        Payment payment = new Payment();
        PgWebhookRequest pgWebhookRequest = new PgWebhookRequest("ORDER123", "SUCCESS");

        when(paymentRepository.findByOrderId("ORDER123")).thenReturn(payment);
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Payment updated = paymentService.updatePayment(pgWebhookRequest);

        // then
        assertEquals(PaymentStatus.SUCCESS, updated.getStatus());
        verify(settlementService, times(1)).settle(eq(updated));
    }

}