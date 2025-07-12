package project.personalproject.domain.chat.chatMessage.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.personalproject.domain.chat.chatMessage.dto.ChatMessageInfo;
import project.personalproject.domain.chat.chatMessage.service.ChatMessageService;
import project.personalproject.domain.chat.chatRoom.dto.ChatRoomInfo;
import project.personalproject.domain.chat.chatRoom.service.ChatRoomService;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.global.security.jwt.JwtService;


import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final JwtService jwtService;

    /**
     * 채팅방 들어가기
     *
     * @return
     */
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<ChatMessageInfo>> findChatMessage(
            @PathVariable("chatId") Long chatId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessage(chatId));
    }

    /**
     * 처음 채팅을 시작하고 싶을 때 사용하는 API
     * 액세스 토큰과 post 에서 두개의 id를 추출하여 채팅방 만듬
     *
     * @param request : 액세스 토큰
     * @return
     */
    @PostMapping("/{postId}/chatRoom")
    public ResponseEntity<ChatRoomInfo> findChatMessageSender(
            HttpServletRequest request,
            @PathVariable("postId") Long postId
    ) {
        Member member = jwtService.getMemberFromToken(request);
        return ResponseEntity.ok(chatRoomService.createChatRoom(member, postId));
    }

    /**
     * 액세스 토큰으로 senderId로 변환하여
     * 채팅방 리스트 보이게 만듬
     *
     * @param request : 액세스 토큰
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomInfo>> findChatRoom(
            HttpServletRequest request
    ) {
        Member member = jwtService.getMemberFromToken(request);
        return ResponseEntity.ok(chatRoomService.getChatRoomByMember(member));
    }


}
