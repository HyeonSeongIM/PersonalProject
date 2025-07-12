package project.personalproject.domain.chat.chatMessage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.personalproject.domain.chat.chatMessage.dto.ChatMessageInfo;
import project.personalproject.domain.chat.chatMessage.entity.ChatMessage;
import project.personalproject.domain.chat.chatMessage.repository.ChatMessageRepository;
import project.personalproject.domain.chat.chatMessage.service.ChatMessageService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<ChatMessageInfo> findChatMessage(Long chatId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatIdOrderByTimestampAsc(chatId);

        return ChatMessageInfo.listOf(chatMessages);
    }

    /**
     * 메세지처리
     * convertAndSendToUser : User에게 직접 메세지 보내기
     * convertAndSend : 해당 채팅방에 메시지 보내기
     *
     * @param chatMessageinfo
     */
    @Override
    public void processMessage(ChatMessageInfo chatMessageinfo) {
        log.info("=== processMessage 시작 ===");
        log.info("처리할 메시지: {}", chatMessageinfo);

        try {
            ChatMessageInfo saveMsg = save(chatMessageinfo);
            log.info("저장 성공: {}", saveMsg);

            String receiverIdStr = String.valueOf(chatMessageinfo.receiverId());
            String senderIdStr = String.valueOf(chatMessageinfo.senderId());

            messagingTemplate.convertAndSendToUser(
                    receiverIdStr,
                    "/queue/messages",
                    saveMsg
            );

            messagingTemplate.convertAndSendToUser(
                    senderIdStr,
                    "/queue/messages",
                    saveMsg
            );

//            messagingTemplate.convertAndSend(
//                    "/topic/chatMessage/"
//                            + chatMessageinfo.senderId()
//                            + chatMessageinfo.receiverId(),
//                    saveMsg);

            log.info("메시지 전송 완료");
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생", e);
            throw e;
        }
        log.info("=== processMessage 완료 ===");
    }

    private ChatMessageInfo save(ChatMessageInfo chatMessageInfo) {

        ChatMessage chatMessage = ChatMessage.from(chatMessageInfo);

        chatMessageRepository.save(chatMessage);

        return ChatMessageInfo.of(chatMessage);
    }
}
