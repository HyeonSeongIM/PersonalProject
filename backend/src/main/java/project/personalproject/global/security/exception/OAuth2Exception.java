package project.personalproject.global.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;


@RequiredArgsConstructor
@Getter
public class OAuth2Exception extends RuntimeException {

    private final ErrorCode errorCode;

}
