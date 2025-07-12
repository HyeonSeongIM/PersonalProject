package project.personalproject.domain.chat.chatMessage.service;



import project.personalproject.domain.chat.chatMessage.dto.ChatMessageInfo;

import java.util.List;

public interface ChatMessageService {

    List<ChatMessageInfo> findChatMessage(Long chatId);

    void processMessage(ChatMessageInfo chatMessageinfo);
}
