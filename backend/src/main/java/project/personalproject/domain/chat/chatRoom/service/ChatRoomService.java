package project.personalproject.domain.chat.chatRoom.service;

import project.personalproject.domain.chat.chatRoom.dto.ChatRoomInfo;
import project.personalproject.domain.member.entity.Member;

import java.util.List;

public interface ChatRoomService {

    List<ChatRoomInfo> getChatRoomByMember(Member member);

    ChatRoomInfo createChatRoom(Member member, Long postId);

//    void exitChatRoom(Long chatRoomId, Member member);
//
//    void deleteChatRoom(Long chatRoomId);
//
//    Long countUnreadMessages(Member member);
}
