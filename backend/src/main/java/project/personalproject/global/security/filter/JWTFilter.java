package project.personalproject.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import project.personalproject.global.redis.repository.BlacklistRepository;
import project.personalproject.global.security.jwt.JwtService;
import project.personalproject.global.security.jwt.JwtUtil;


import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final BlacklistRepository blacklistRepository;

    // 접근 제한자
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 추출
        String accessToken = jwtService.resolveAccessToken(request);

        String requestURI = request.getRequestURI();
        // Prometheus 메트릭 요청은 JWT 검증 제외
        if (requestURI.equals("/metrics") || requestURI.startsWith("/actuator/")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰이 없으면 다음 필터로 진행
        if (accessToken == null) {
            log.info("Request: {} {}, User-Agent: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getHeader("User-Agent"));

            filterChain.doFilter(request, response);

            return;
        }

        // blacklist 체크
        if (blacklistRepository.existsById(accessToken)) {
            log.warn("[JWTFilter] Blacklisted access token [{}]", accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료시
        if (jwtUtil.isExpired(accessToken)){
            log.warn("[JwtFilter] Token is Expired, proceeding reissue.");

            response.setStatus(401);
            return;
        }

        // 여기서 refresh 와 access 나눠야함
        String category = jwtUtil.getCategory(accessToken);
        if (category == null || category.equals("refresh")) {
            return;
        }

        Authentication authentication = jwtUtil.getAuthentication(accessToken);

        // 토큰 유효할 경우 User의 권한을 발급
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[JWTFilter] Token validation successful. User authenticated: {}", authentication.getName());
        }

        filterChain.doFilter(request, response);
    }
}