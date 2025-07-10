package project.personalproject.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import project.personalproject.domain.member.dto.MemberInfo;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.member.entity.Role;

public interface JwtService {
    String resolveAccessToken(HttpServletRequest request);

    String resolveRefreshToken(HttpServletRequest request);

    String generateAccessToken(String category, String verifyKey, String username, String email, Role role, Long expiredMs);

    String generateRefreshToken(Long expiredMs);

    String generateNewAccessToken(MemberInfo memberInfo);

    String generateNewRefreshToken();

    Member getMemberFromToken(HttpServletRequest request);
}
