package project.personalproject.domain.chat.chatParticipant.entity;

import jakarta.persistence.*;
import lombok.*;
import project.personalproject.domain.chat.chatRoom.entity.ChatRoom;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private String memberId;  // 참여자

    public static ChatParticipant from(ChatRoom chatRoom, String memberId) {
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .memberId(memberId)
                .build();
    }
}
