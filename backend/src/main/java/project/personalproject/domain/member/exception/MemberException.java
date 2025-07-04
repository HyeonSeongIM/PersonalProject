package project.personalproject.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public class MemberException extends RuntimeException {
    private final ErrorCode errorCode;
}
