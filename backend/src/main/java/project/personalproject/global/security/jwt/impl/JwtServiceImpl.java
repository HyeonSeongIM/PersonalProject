package project.personalproject.global.security.jwt.impl;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.personalproject.domain.member.dto.MemberInfo;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.member.entity.Role;
import project.personalproject.domain.member.repository.MemberRepository;
import project.personalproject.global.security.jwt.JwtService;
import project.personalproject.global.security.jwt.JwtUtil;
import project.personalproject.global.security.service.TokenResponseUtil;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;
    private final TokenResponseUtil tokenResponseUtil;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public JwtServiceImpl(@Value("${spring.jwt.secret}") String secret, TokenResponseUtil tokenResponseUtil, JwtUtil jwtUtil, MemberRepository memberRepository) {
        secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.tokenResponseUtil = tokenResponseUtil;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim(); // "Bearer " 이후의 토큰 값만 가져옴
        }
        return null;
    }

    @Override
    public String resolveRefreshToken(HttpServletRequest request) {
       return tokenResponseUtil.getRefreshToken(request);
    }

    @Override
    public String generateAccessToken(String category, String verifyKey, String username, String provider, String email, Role role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("verifyKey", verifyKey)
                .claim("username", username)
                .claim("provider", provider)
                .claim("email", email)
                .claim("Role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(Long expiredMs){
        return Jwts.builder()
                .claim("uuid", UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateNewAccessToken(MemberInfo memberInfo) {
        String verifyKey = memberInfo.verifyKey();
        String username = memberInfo.username();
        String email = memberInfo.email();
        Role role = memberInfo.role();
        String provider = memberInfo.provider();

        return generateAccessToken("access", verifyKey, username, provider ,email, role, 10 * 60 * 1000L);
    }

    @Override
    public String generateNewRefreshToken() {
        return generateRefreshToken(60 * 60 * 1000L);
    }

    @Override
    public Member getMemberFromToken(HttpServletRequest request) {
        String verifyKey = jwtUtil.getVerifyKey(resolveAccessToken(request));

        return memberRepository.findByVerifyKey(verifyKey);
    }

    @Override
    public Member getMemberFromTokenWithEmail(HttpServletRequest request) {
        String email = jwtUtil.getEmail(resolveAccessToken(request));

        return memberRepository.findByEmail(email);
    }

    @Override
    public Member getMemberFromTokenWithKeyAndEmail(HttpServletRequest request) {
        String verifyKey = jwtUtil.getVerifyKey(resolveAccessToken(request));
        String email = jwtUtil.getEmail(resolveAccessToken(request));

        return memberRepository.findByVerifyKeyAndEmail(verifyKey, email);
    }

    @Override
    public Member getMemberFromTokenWithProviderAndEmail(HttpServletRequest request) {
        String provider = jwtUtil.getProvider(resolveAccessToken(request));
        String email = jwtUtil.getEmail(resolveAccessToken(request));

        return memberRepository.findByProviderAndEmail(provider, email);
    }


}
