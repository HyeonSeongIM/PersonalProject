package project.personalproject.domain.chat.chatParticipant.service;

import project.personalproject.domain.chat.chatRoom.entity.ChatRoom;
import project.personalproject.domain.member.entity.Member;

public interface ChatParticipantService {
    void joinParticipants(ChatRoom chatRoom, Member member, Long postId);
}
