package project.personalproject.domain.chat.chatRoom.dto;


import project.personalproject.domain.chat.chatRoom.entity.ChatRoom;

import java.util.List;
import java.util.stream.Collectors;

public record ChatRoomInfo(
        Long chatRoomId
) {
    public static ChatRoomInfo of(ChatRoom chatRoom) {
        return new ChatRoomInfo(
                chatRoom.getId()
        );
    }

    public static List<ChatRoomInfo> listOf(List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .map(ChatRoomInfo::of)
                .collect(Collectors.toList());
    }
}
