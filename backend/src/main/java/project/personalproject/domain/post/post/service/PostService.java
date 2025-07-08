package project.personalproject.domain.post.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;

import java.util.List;

public interface PostService {

    // 게시글 생성
    PostResponse createPost(CreatePostCommand postRequest, Member member, List<MultipartFile> images) throws Exception;

    // 게시글 수정
    PostResponse updatePost(Long postId, UpdatePostCommand postRequest, Member member, List<MultipartFile> images);

    // 게시글 삭제
    PostResponse deletePost(Long postId, Member member);

    // 게시글 하나 가져오기
    PostResponse getPost(Long postId);

    // 게시글 전체 가져오기
    Page<PostResponse> getPostList(Pageable pageable);
}
