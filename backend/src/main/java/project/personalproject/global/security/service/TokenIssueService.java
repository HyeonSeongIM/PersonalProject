package project.personalproject.global.security.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import project.personalproject.domain.member.dto.OAuth2Info;
import project.personalproject.domain.member.dto.TokenResponse;
import project.personalproject.domain.member.entity.Role;
import project.personalproject.global.redis.service.RefreshTokenService;
import project.personalproject.global.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class TokenIssueService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenResponseUtil tokenResponseUtil;

    public TokenResponse issueTokens(OAuth2Info userInfo, HttpServletResponse response) {
        String verifyKey = userInfo.getVerifyKey();
        String username = userInfo.getName();
        String provider = userInfo.getProvider();
        String email = userInfo.getEmail();
        Role role = userInfo.getRole();

        String accessToken = jwtService.generateAccessToken("access", verifyKey, username, provider,email, role, 30 * 60 * 1000L); // 30분
        String refreshToken = jwtService.generateRefreshToken(7 * 24 * 60 * 60 * 1000L); // 7일

        refreshTokenService.saveRefreshToken(verifyKey, refreshToken);

        ResponseCookie refreshCookie = tokenResponseUtil.createCookie("refresh", refreshToken);
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return TokenResponse.of(accessToken);
    }


}
