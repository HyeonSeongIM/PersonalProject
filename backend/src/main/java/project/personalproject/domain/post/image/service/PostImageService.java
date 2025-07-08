package project.personalproject.domain.post.image.service;

import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.post.image.dto.response.PostImageResponse;

import java.util.List;

public interface PostImageService {

    // 이미지 업로드
    PostImageResponse createImages(List<MultipartFile> images) throws Exception;

    // 이미지 수정
    PostImageResponse updateImages(Long postId, List<MultipartFile> images) throws Exception;

    // 이미지 삭제
    void deleteImages(Long postId) throws Exception;
}
