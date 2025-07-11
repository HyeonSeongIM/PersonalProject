package project.personalproject.domain.post.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.member.service.MemberService;
import project.personalproject.domain.post.comment.dto.response.PostCommentResponse;
import project.personalproject.domain.post.comment.service.PostCommentService;
import project.personalproject.domain.post.image.service.PostImageService;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;
import project.personalproject.domain.post.post.dto.response.PostWithCommentsResponse;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.exception.PostException;
import project.personalproject.domain.post.post.repository.PostRepository;
import project.personalproject.domain.post.post.service.PostService;
import project.personalproject.domain.search.service.SearchIndexService;
import project.personalproject.global.exception.ErrorCode;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final PostCommentService postCommentService;
    private final SearchIndexService searchIndexService;

    /**
     * 게시글 생성
     *
     * @param postRequest 생성 요청 DTO
     * @param member      작성자 정보
     * @return 생성된 게시글 정보
     */
    @Override
    public PostResponse createPost(CreatePostCommand postRequest, Member member, List<MultipartFile> images) throws Exception {
        Post post = Post.from(postRequest, member);
        postRepository.save(post);
        postImageService.createImages(post,images);
        searchIndexService.indexPost(post);
        return PostResponse.of(post);
    }

    /**
     * 게시글 수정
     * 작성자 본인만 수정 가능
     *
     * @param postId      게시글 ID
     * @param postRequest 수정 요청 DTO
     * @param member      요청자 정보
     * @return 수정된 게시글 정보
     */
    @Override
    public PostResponse updatePost(Long postId, UpdatePostCommand postRequest, Member member, List<MultipartFile> images) {
        Post post = getPostIfSameUser(postId, member);
        Post newPost = Post.updateFrom(post, postRequest); // 추후 더티 체킹 방식으로 리팩토링 가능
        postRepository.save(newPost);
        searchIndexService.updatePost(newPost);
        return PostResponse.of(newPost);
    }

    /**
     * 게시글 삭제
     * 작성자 본인만 삭제 가능
     *
     * @param postId 게시글 ID
     * @param member 요청자 정보
     * @return 삭제된 게시글 정보
     */
    @Override
    public PostResponse deletePost(Long postId, Member member) {
        Post post = getPostIfSameUser(postId, member);
        postRepository.delete(post);
        searchIndexService.deletePost(postId);
        return PostResponse.of(post);
    }

    /**
     * 게시글 목록 조회 (페이징 지원)
     *
     * @param pageable 페이지 정보
     * @return 게시글 페이지 (PostResponse)
     */
    @Transactional(readOnly = true)
    @Override
    public Page<PostResponse> getPostList(Pageable pageable) {
        Page<Post> post = postRepository.findAllOrThrow(pageable);
        return PostResponse.pageOf(post);
    }

    /**
     * 게시글과 댓글 조회
     *
     * @param postId 게시글 ID
     * @return 게시글과 해당 댓글들
     */
    @Transactional(readOnly = true)
    @Override
    public PostWithCommentsResponse getPostWithComments(Long postId, Pageable pageable) {
        PostResponse post = getPost(postId); // 기존 게시글 단건 조회
        Page<PostCommentResponse> comments = postCommentService.getCommentByPost(postId, pageable);
        return PostWithCommentsResponse.of(post, comments);
    }

    /**
     * 단일 게시글 조회
     *
     * @param postId 게시글 ID
     * @return 게시글 상세 정보
     */
    private PostResponse getPost(Long postId) {
        Post post = postRepository.findByIdOrThrow(postId);
        return PostResponse.of(post);
    }

    /**
     * 게시글 작성자 본인 여부 확인
     * 작성자가 아닐 경우 예외 발생
     *
     * @param postId 게시글 ID
     * @param member 요청자 정보
     * @return 게시글 객체
     */
    private Post getPostIfSameUser(Long postId, Member member) {
        Post post = postRepository.findByIdOrThrow(postId);
        if (!post.getMember().equals(member)) {
            throw new PostException(ErrorCode.NOT_MATCH_USER);
        }
        return post;
    }
}
