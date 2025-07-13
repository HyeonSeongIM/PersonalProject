package project.personalproject.domain.chat.chatRoom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 채팅방 고유 ID

    private Long postId; // 게시글 ID

    public static ChatRoom from(Long postId) {
        return ChatRoom.builder()
                .postId(postId)
                .build();
    }
}
