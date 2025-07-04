package project.personalproject.global.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.personalproject.domain.member.dto.auth.GoogleResponse;
import project.personalproject.domain.member.dto.auth.KakaoResponse;
import project.personalproject.domain.member.dto.auth.NaverResponse;
import project.personalproject.domain.member.dto.auth.OAuth2Response;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    private final SocialMemberService socialMemberService;
    private final ObjectMapper objectMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        // OAuth2User 값 받아오기
        OAuth2User oAuth2User = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        switch (registrationId) {
            case "kakao" -> oAuth2Response = objectMapper.convertValue(oAuth2User.getAttributes(), KakaoResponse.class);
            case "google" -> oAuth2Response = objectMapper.convertValue(oAuth2User.getAttributes(), GoogleResponse.class);
            case "naver" -> oAuth2Response = objectMapper.convertValue(oAuth2User.getAttributes(), NaverResponse.class);
            default -> throw new IllegalArgumentException("지원되지 않는 OAuth2 로그인 제공자: " + registrationId);
        }

        return socialMemberService.findOrCreateMember(oAuth2Response);
    }

}
