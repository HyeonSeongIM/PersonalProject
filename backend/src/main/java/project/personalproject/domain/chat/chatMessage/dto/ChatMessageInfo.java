package project.personalproject.domain.chat.chatMessage.dto;

import project.personalproject.domain.chat.chatMessage.entity.ChatMessage;

import java.util.List;
import java.util.stream.Collectors;

public record ChatMessageInfo(
        Long chatId,
        String senderId,
        String receiverId,
        String message
) {
    public static ChatMessageInfo of(ChatMessage chatMessage) {
        return new ChatMessageInfo(
                chatMessage.getChatId(),
                chatMessage.getSenderId(),
                chatMessage.getReceiverId(),
                chatMessage.getMessage());
    }

    public static List<ChatMessageInfo> listOf(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(ChatMessageInfo::of)
                .collect(Collectors.toList());
    }
}
