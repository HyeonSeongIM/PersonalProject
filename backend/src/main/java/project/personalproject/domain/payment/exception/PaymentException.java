package project.personalproject.domain.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public class PaymentException extends RuntimeException {
    private final ErrorCode errorCode;
}
