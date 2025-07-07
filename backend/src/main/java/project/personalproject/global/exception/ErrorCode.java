package project.personalproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // 게시글 처리 에러코드
    NOT_FOUND_POST(100, "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_USER(101, "작성자만 요청할 수 있습니다.", HttpStatus.BAD_REQUEST);

    private final int errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
