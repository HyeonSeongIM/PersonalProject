package project.personalproject.domain.chat.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.repository.PostRepository;

@Component
@RequiredArgsConstructor
public class ChatMemberResolver {

    private final PostRepository postRepository;

    public String getSenderId(Member member) {
        return member.getId().toString();
    }

    public String getReceiverId(Long postId) {
        Post post = postRepository.findByIdOrThrow(postId);
        return post.getMember().getId().toString();
    }
}
