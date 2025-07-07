package project.personalproject.global.redis.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.personalproject.global.redis.domain.Blacklist;
import project.personalproject.global.redis.repository.BlacklistRepository;
import project.personalproject.global.security.jwt.JwtService;
import project.personalproject.global.security.jwt.JwtUtil;


@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistService {
    private final BlacklistRepository blacklistRepository;
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;

    /**
     * 로그아웃 한 사용자 액세스 토큰은 블랙리스트에 넣어
     * 접근을 할 수 없게 만듭니다.
     * @param request
     */
    public void createBlacklist(HttpServletRequest request) {
        String accessToken = jwtService.resolveAccessToken(request);

        String verifyKey = jwtUtil.getVerifyKey(accessToken);

        Blacklist blacklist = Blacklist.from(verifyKey, accessToken);

        blacklistRepository.save(blacklist);
    }
}
