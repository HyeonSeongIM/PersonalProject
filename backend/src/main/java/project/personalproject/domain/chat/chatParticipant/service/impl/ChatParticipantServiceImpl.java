package project.personalproject.domain.chat.chatParticipant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.chat.chatParticipant.entity.ChatParticipant;
import project.personalproject.domain.chat.chatParticipant.repository.ChatParticipantRepository;
import project.personalproject.domain.chat.chatParticipant.service.ChatParticipantService;
import project.personalproject.domain.chat.chatRoom.entity.ChatRoom;
import project.personalproject.domain.chat.support.ChatMemberResolver;
import project.personalproject.domain.member.entity.Member;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatParticipantServiceImpl implements ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMemberResolver chatMemberResolver;

    @Override
    public void joinParticipants(ChatRoom chatRoom, Member member, Long postId) {
        String senderId = chatMemberResolver.getSenderId(member);
        String receiverId = chatMemberResolver.getReceiverId(postId);

        ChatParticipant sender = ChatParticipant.from(chatRoom, senderId);
        ChatParticipant receiver = ChatParticipant.from(chatRoom, receiverId);

        chatParticipantRepository.save(sender);
        chatParticipantRepository.save(receiver);
    }

}
