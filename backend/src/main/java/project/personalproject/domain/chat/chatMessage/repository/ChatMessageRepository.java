package project.personalproject.domain.chat.chatMessage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.personalproject.domain.chat.chatMessage.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByChatIdOrderByTimestampAsc(Long chatId);
}
