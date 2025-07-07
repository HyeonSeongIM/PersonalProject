package project.personalproject.global.redis.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.personalproject.domain.member.dto.MemberInfo;

public interface RefreshTokenService {
    // refresh 토큰을 Redis에 저장
    void saveRefreshToken(String id, String refreshToken);

    // RefreshToken 삭제
    void deleteRefreshToken(HttpServletRequest request, HttpServletResponse response);

    // value 값 가져오기
    String getRefreshToken(String refreshToken);

    // 사용자 정보 가져오기
    MemberInfo getMemberInfoFromRefreshToken(HttpServletRequest request);
}
