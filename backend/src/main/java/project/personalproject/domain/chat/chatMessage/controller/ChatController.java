package project.personalproject.domain.chat.chatMessage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import project.personalproject.domain.chat.chatMessage.dto.ChatMessageInfo;
import project.personalproject.domain.chat.chatMessage.service.ChatMessageService;


@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatMessageService chatMessageService;

    /**
     * 메시지 보내기
     *
     * @param chatMessageinfo : sender, receiver, content
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageInfo chatMessageinfo) {
        chatMessageService.processMessage(chatMessageinfo);
    }
}
