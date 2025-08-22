package project.personalproject.domain.post.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.image.exception.PostImageException;
import project.personalproject.domain.post.image.service.PostImageService;
import project.personalproject.domain.post.post.dto.PostDTO;
import project.personalproject.domain.post.post.dto.PostListDTO;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.exception.PostException;
import project.personalproject.domain.post.post.repository.PostRepository;
import project.personalproject.domain.post.post.service.PostService;
import project.personalproject.domain.search.service.SearchIndexService;
import project.personalproject.global.exception.ErrorCode;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final SearchIndexService searchIndexService;

    /**
     * 게시글 생성
     * 1. 이미지 저장
     * 2. 게시글 생성
     * 3. 전체 저장
     *
     * @param postRequest 생성 요청 DTO
     * @param member      작성자 정보
     * @return 생성된 게시글 정보
     */
    @Transactional(rollbackFor = {PostException.class, PostImageException.class})
    @Override
    public PostResponse createPost(CreatePostCommand postRequest, Member member, List<MultipartFile> images) throws Exception {
        List<String> imageNames = postImageService.uploadImages(images);

        Post post = Post.from(postRequest, member);

        postRepository.save(post);

        postImageService.saveImages(post, imageNames);

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

        Post newPost = postRepository.findByIdOrThrow(post.getId());

        newPost.setTitle(postRequest.title());
        newPost.setContent(postRequest.content());

        searchIndexService.indexPost(newPost);

        return PostResponse.of(newPost);
    }
//    Post newPost = Post.updateFrom(post, postRequest); // 추후 더티 체킹 방식으로 리팩토링 가능
//        postRepository.save(newPost);
//        searchIndexService.updatePost(newPost);

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
    public PostListDTO getPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAllOrThrow(pageable);

        return PostListDTO.of(
                PostDTO.pageOf(posts)
        );
    }

    /**
     * 단일 게시글 조회
     *
     * @param postId 게시글 ID
     * @return 게시글 상세 정보
     */
    @Transactional(readOnly = true)
    @Override
    public PostDTO getPost(Long postId) {
        Post post = postRepository.findByIdOrThrow(postId);
        return PostDTO.of(post);
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
