package project.personalproject.domain.post.post.service;

import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;

public interface PostService {

    // 게시글 생성
    PostResponse createPost (CreatePostCommand postRequest, Member member);

    // 게시글 수정
    PostResponse updatePost (Long postId, UpdatePostCommand postRequest, Member member);

    // 게시글 삭제
    PostResponse deletePost (Long postId, Member member);
}
