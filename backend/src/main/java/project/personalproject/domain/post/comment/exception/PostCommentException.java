package project.personalproject.domain.post.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class PostCommentException extends RuntimeException {
    private final ErrorCode errorCode;
}
