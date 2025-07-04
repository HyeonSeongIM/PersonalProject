package project.personalproject.domain.member.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.personalproject.domain.member.dto.OAuth2Info;
import project.personalproject.domain.member.dto.TokenResponse;
import project.personalproject.domain.member.dto.auth.GoogleResponse;
import project.personalproject.domain.member.dto.auth.KakaoResponse;
import project.personalproject.domain.member.dto.auth.NaverResponse;
import project.personalproject.domain.member.dto.auth.OAuth2Response;
import project.personalproject.domain.member.service.MemberService;
import project.personalproject.global.redis.service.RefreshTokenService;
import project.personalproject.global.redis.service.impl.BlacklistService;
import project.personalproject.global.security.service.SocialMemberService;
import project.personalproject.global.security.service.TokenIssueService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final RefreshTokenService refreshTokenService;
    private final BlacklistService blacklistService;
    private final ObjectMapper objectMapper;
    private final SocialMemberService socialMemberService;
    private final TokenIssueService tokenIssueService;

    // 로그인
    @Override
    public TokenResponse signIn(String provider, HttpServletRequest request, HttpServletResponse response) throws IOException {

        OAuth2Response oAuth2Response = parseResponse(provider, request); // 소셜에 맞는 데이터로 변환

        OAuth2Info oAuth2Info = socialMemberService.findOrCreateMember(oAuth2Response); // DB 저장 DTO

        return tokenIssueService.issueTokens(oAuth2Info, response);
    }

    // 로그아웃
    @Override
    public void signOut(HttpServletRequest request, HttpServletResponse response) {

        blacklistService.createBlacklist(request);

        refreshTokenService.deleteRefreshToken(request, response);
    }

    // 이거는 APP 에서 실행할 때 사용
    public OAuth2Response parseResponse(String provider, HttpServletRequest request) throws IOException {
        return switch (provider.toLowerCase()) {
            case "google" -> objectMapper.readValue(request.getInputStream(), GoogleResponse.class);
            case "kakao" -> objectMapper.readValue(request.getInputStream(), KakaoResponse.class);
            case "naver" -> objectMapper.readValue(request.getInputStream(), NaverResponse.class);
            default -> throw new IllegalAccessError("잘못된 소셜 제공자입니다.");
        };
    }

}
