package project.personalproject.domain.post.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.exception.PostException;
import project.personalproject.domain.post.post.repository.PostRepository;
import project.personalproject.domain.post.post.service.PostService;
import project.personalproject.global.exception.ErrorCode;

@RequiredArgsConstructor
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostResponse createPost(CreatePostCommand postRequest, Member member) {
        Post post = Post.from(postRequest, member);

        postRepository.save(post);

        return PostResponse.of(post);
    }

    @Override
    public PostResponse updatePost(Long postId, UpdatePostCommand postRequest, Member member) {




        return null;
    }

    @Override
    public PostResponse deletePost(Long postId, Member member) {
        return null;
    }

    // 요청한 사용자가 해당 게시글의 작성자와 같은지 확인하는 로직
    private Post getPostIfSameUser(Long postId, Member member) {
        Post post = postRepository.getByIdOrThrow(postId);

        if (!post.getMember().equals(member)){
            throw new PostException(ErrorCode.NOT_MATCH_USER);
        }

        return post;
    }
}
