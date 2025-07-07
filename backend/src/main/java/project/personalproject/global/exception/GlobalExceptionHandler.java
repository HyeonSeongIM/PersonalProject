package project.personalproject.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.personalproject.domain.post.post.exception.PostException;
import project.personalproject.global.exception.dto.ErrorResponseV0;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponseV0> handlePostException(PostException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponseV0.of(errorCode));
    }

}
