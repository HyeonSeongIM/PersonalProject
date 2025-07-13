package project.personalproject.domain.chat.chatMessage.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.personalproject.global.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public class ChatException extends RuntimeException {
    private final ErrorCode errorCode;
}
