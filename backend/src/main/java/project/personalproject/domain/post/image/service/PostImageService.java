package project.personalproject.domain.post.image.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.post.image.dto.PostImageListDTO;
import project.personalproject.domain.post.image.dto.response.PostImageResponse;
import project.personalproject.domain.post.post.entity.Post;

import java.util.List;

public interface PostImageService {

    // 이미지 가져오기
    PostImageListDTO getPostImageByPostId(Long id, Pageable pageable);

    // 이미지 업로드
    void saveImages(Post post, List<String> images) throws Exception;

    // 이미지 수정
    void updateImages(Long postId, List<MultipartFile> images) throws Exception;

    // 이미지 삭제
    void deleteImages(Long postId) throws Exception;

    List<String> uploadImages(List<MultipartFile> images) throws Exception;

}
