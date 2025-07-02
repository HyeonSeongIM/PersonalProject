package project.personalproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // 게시글 처리 에러코드
    INVALID_POST_REQUEST(100, "제목에 값을 입력해주세요!", HttpStatus.BAD_REQUEST),
    ;

    private final int errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
