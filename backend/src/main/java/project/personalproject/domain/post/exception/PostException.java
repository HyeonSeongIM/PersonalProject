package project.personalproject.domain.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public class PostException extends RuntimeException {
    private final ErrorCode errorCode;
}
