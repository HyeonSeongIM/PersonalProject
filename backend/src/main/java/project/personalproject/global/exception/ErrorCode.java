package project.personalproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // 게시글 처리 에러코드
    NOT_FOUND_POST(100, "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_USER(101, "작성자만 요청할 수 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_POST_ALL(102, "게시글들이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 게시글 댓글 에러코드
    NOT_FOUND_COMMENT(200, "해당 댓글을 조회할 수 없습니다.", HttpStatus.NOT_FOUND),

    // 검색 에러 코드

    // 결제 에러 코드
    NOT_FOUND_ORDER_ID(400, "해당 결제건을 조회할 수 없습니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
