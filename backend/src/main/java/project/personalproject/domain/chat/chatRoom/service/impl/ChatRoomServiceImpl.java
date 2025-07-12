package project.personalproject.domain.chat.chatRoom.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.personalproject.domain.chat.chatParticipant.service.ChatParticipantService;
import project.personalproject.domain.chat.chatRoom.dto.ChatRoomInfo;
import project.personalproject.domain.chat.chatRoom.entity.ChatRoom;
import project.personalproject.domain.chat.chatRoom.repository.ChatRoomRepository;
import project.personalproject.domain.chat.chatRoom.service.ChatRoomService;
import project.personalproject.domain.chat.support.ChatMemberResolver;
import project.personalproject.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantService chatParticipantService;
    private final ChatMemberResolver chatMemberResolver;

    @Override
    public List<ChatRoomInfo> getChatRoomByMember(Member member) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByMemberId(member.getId().toString());

        return ChatRoomInfo.listOf(chatRooms);
    }

    @Override
    public ChatRoomInfo createChatRoom(Member member, Long postId) {
        Optional<ChatRoom> chatRoom = getChatRoom(member, postId);

        if (chatRoom.isPresent()) {
            return ChatRoomInfo.of(chatRoom.get());
        }

        ChatRoom newChatRoom = ChatRoom.from(postId);

        chatRoomRepository.save(newChatRoom);

        chatParticipantService.joinParticipants(newChatRoom, member, postId);

        return ChatRoomInfo.of(newChatRoom);
    }

    private Optional<ChatRoom> getChatRoom(Member member, Long postId) {
        String senderId = chatMemberResolver.getSenderId(member);

        String receiverId = chatMemberResolver.getReceiverId(postId);

        return chatRoomRepository.findByPostIdAndParticipants(postId, senderId, receiverId);
    }


}
