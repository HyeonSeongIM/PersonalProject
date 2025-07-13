package project.personalproject.domain.payment.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.entity.Payment;
import project.personalproject.domain.payment.entity.PaymentStatus;
import project.personalproject.domain.payment.repository.PaymentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void 펜딩의_변화가_적절한지_확인() {
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

}