package project.personalproject.domain.chat.chatRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.personalproject.domain.chat.chatRoom.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    /**
     * 채팅방이 있는지 확인
     */
    @Query("""
            SELECT r FROM ChatRoom r
            JOIN ChatParticipant p1 ON p1.chatRoom = r AND p1.memberId = :memberId1
            JOIN ChatParticipant p2 ON p2.chatRoom = r AND p2.memberId = :memberId2
            WHERE r.postId = :postId
            """)
    Optional<ChatRoom> findByPostIdAndParticipants(Long postId, String memberId1, String memberId2);

    @Query("""
                SELECT cp.chatRoom FROM ChatParticipant cp
                WHERE cp.memberId = :memberId
            """)
    List<ChatRoom> findChatRoomsByMemberId(String memberId);

}
