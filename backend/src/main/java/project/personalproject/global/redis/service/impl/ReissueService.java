package project.personalproject.global.redis.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import project.personalproject.domain.member.dto.MemberInfo;
import project.personalproject.domain.member.dto.TokenResponse;
import project.personalproject.global.redis.service.RefreshTokenService;
import project.personalproject.global.security.jwt.JwtService;
import project.personalproject.global.security.service.TokenResponseUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReissueService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenResponseUtil tokenResponseUtil;

    public TokenResponse reissue(HttpServletRequest request, HttpServletResponse response) {

        MemberInfo member = refreshTokenService.getMemberInfoFromRefreshToken(request);

        String newAccessToken = jwtService.generateNewAccessToken(member);

        rotateRefreshToken(request, response);

        return TokenResponse.of(newAccessToken);
    }

    private void rotateRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        MemberInfo memberInfo = refreshTokenService.getMemberInfoFromRefreshToken(request);

        refreshTokenService.deleteRefreshToken(request, response);

        String newRefreshToken = jwtService.generateNewRefreshToken();

        refreshTokenService.saveRefreshToken(memberInfo.verifyKey(), newRefreshToken);

        ResponseCookie cookie = tokenResponseUtil.createCookie("refresh", newRefreshToken);

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
