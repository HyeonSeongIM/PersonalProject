package project.personalproject.domain.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.personalproject.domain.post.dto.request.PostRequest;
import project.personalproject.domain.post.entity.Post;
import project.personalproject.domain.post.exception.PostException;
import project.personalproject.domain.post.repository.PostRepository;
import project.personalproject.domain.post.service.PostService;
import project.personalproject.global.exception.ErrorCode;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    // TODO : 게시글 생성 로직 작성
    public Long create(PostRequest postRequest) {

        if (postRequest.title() == null || postRequest.title().isEmpty()) {
            throw new PostException(ErrorCode.INVALID_POST_REQUEST);
        }

        Post post = new Post();
        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());

        return postRepository.save(post).getId();
    }
}
