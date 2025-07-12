package project.personalproject.domain.chat.chatMessage.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import project.personalproject.domain.chat.chatMessage.dto.ChatMessageInfo;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_message")
public class ChatMessage {

    @Id
    private String id; // 채팅메시지 고유 ID

    private Long chatId; // 채팅방 ID

    private String senderId; // 보내는 사람

    private String receiverId; // 받는 사람

    private String message; // 내용

    private LocalDateTime timestamp; // 시간

    /**
     * 채팅 메세지 생성 빌더
     * @param chatMessageInfo
     * @return
     */
    public static ChatMessage from(ChatMessageInfo chatMessageInfo) {
        return ChatMessage.builder()
                .chatId(chatMessageInfo.chatId())
                .senderId(chatMessageInfo.senderId())
                .receiverId(chatMessageInfo.receiverId())
                .message(chatMessageInfo.message())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
