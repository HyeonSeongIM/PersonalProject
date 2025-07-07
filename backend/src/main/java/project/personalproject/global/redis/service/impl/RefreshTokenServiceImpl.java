package project.personalproject.global.redis.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.personalproject.domain.member.dto.MemberInfo;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.member.repository.MemberRepository;
import project.personalproject.domain.member.service.MemberService;
import project.personalproject.global.redis.domain.RefreshToken;
import project.personalproject.global.redis.repository.RefreshTokenRepository;
import project.personalproject.global.redis.service.RefreshTokenService;
import project.personalproject.global.security.jwt.JwtService;
import project.personalproject.global.security.service.TokenResponseUtil;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenResponseUtil tokenResponseUtil;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    // 리프레쉬 토큰을 레디스에 저장합니다.
    @Override
    public void saveRefreshToken(String id, String refreshToken) {

        RefreshToken token = RefreshToken.from(id, refreshToken);

        refreshTokenRepository.save(token);
    }

    // 리프레쉬 토큰을 삭제합니다.
    @Override
    public void deleteRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = tokenResponseUtil.getRefreshToken(request);

        refreshTokenRepository.deleteById(refreshToken);

        tokenResponseUtil.expiredCookie(response);
    }

    // 레디스 토큰을 가져옵니다.
    @Override
    public String getRefreshToken(String refreshToken) {
        Optional<RefreshToken> refresh = refreshTokenRepository.findById(refreshToken);

        return refresh.get().getVerifyKey();
    }

    // 리프레쉬 토큰으로 사용자 정보를 가져옵니다.
    @Override
    public MemberInfo getMemberInfoFromRefreshToken(HttpServletRequest request) {

        String refreshToken = jwtService.resolveRefreshToken(request);

        String verifyKey = getRefreshToken(refreshToken);

        Member member = memberRepository.findByVerifyKey(verifyKey);

        return MemberInfo.of(member);

    }
}
