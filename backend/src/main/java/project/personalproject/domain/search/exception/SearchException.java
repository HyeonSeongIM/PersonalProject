package project.personalproject.domain.search.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class SearchException extends RuntimeException {
    private final ErrorCode errorCode;
}
