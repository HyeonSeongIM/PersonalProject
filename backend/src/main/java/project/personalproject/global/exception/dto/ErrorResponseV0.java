package project.personalproject.global.exception.dto;

import lombok.Builder;
import org.springframework.validation.BindingResult;
import project.personalproject.global.exception.ErrorCode;

import java.util.List;

@Builder
public record ErrorResponseV0(
        String name,
        int errorCode,
        String message,
        List<FieldError> errors
) {

    // 도메인 & 서비스 에러
    public static ErrorResponseV0 of(ErrorCode errorCode) {
        return ErrorResponseV0.builder()
                .name(errorCode.name())
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .build();
    }

    // @Valid 바디 검증 실패
    public static ErrorResponseV0 ofValidation(BindingResult br) {
        List<FieldError> list = br.getFieldErrors().stream()
                .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return ErrorResponseV0.builder()
                .name("BAD_REQUEST")
                .errorCode(400)
                .message("Validation failed")
                .errors(list)
                .build();
    }

}
