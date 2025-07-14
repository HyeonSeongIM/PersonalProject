package project.personalproject.domain.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class PgException extends RuntimeException {
    private final ErrorCode errorCode;
}
