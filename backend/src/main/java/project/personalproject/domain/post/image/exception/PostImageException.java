package project.personalproject.domain.post.image.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class PostImageException extends RuntimeException {
  private final ErrorCode errorCode;
}
