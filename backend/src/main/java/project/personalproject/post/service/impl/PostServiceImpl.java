package project.personalproject.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.personalproject.post.dto.request.PostRequest;
import project.personalproject.post.repository.PostRepository;
import project.personalproject.post.service.PostService;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    // TODO : 게시글 생성 로직 작성
    public Long create(PostRequest postRequest) {
        throw new UnsupportedOperationException("아직 구현되지 않음");
    }
}
