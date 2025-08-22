package project.personalproject.domain.post.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostImageIoService {
    // MiniO에 파일 올리기
    List<String> uploadToMinIO(List<MultipartFile> images) throws Exception;

    // MiniO에 이미지 삭제
    void deleteImages(List<String> imageNames) throws Exception;

    // 이미지 이름 URL로 변환
    List<String> convertToUrls(List<String> imageNames) throws Exception;
}
