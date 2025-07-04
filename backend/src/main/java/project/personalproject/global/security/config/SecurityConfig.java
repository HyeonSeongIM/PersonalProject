package project.personalproject.global.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.ForwardedHeaderFilter;
import project.personalproject.global.redis.repository.BlacklistRepository;
import project.personalproject.global.security.filter.JWTFilter;
import project.personalproject.global.security.handler.SuccessHandler;
import project.personalproject.global.security.jwt.JwtService;
import project.personalproject.global.security.jwt.JwtUtil;
import project.personalproject.global.security.service.CustomOAuth2Service;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
@Slf4j
public class SecurityConfig {

    private final CustomOAuth2Service customOAuth2Service;
    private final SuccessHandler successHandler;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final BlacklistRepository blacklistRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //oauth2
        http
                .oauth2Login(oauth2 -> oauth2// 커스텀 로그인 페이지
                        .failureUrl("/login?error=true")     // 실패 시 리다이렉트
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2Service)) // 커스텀 OAuth2UserService 설정
                        .successHandler(successHandler)
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository()) // yml 파일에 두지 않고 커스텀 형태로 만듬 + 인메모리 형식으로 저장
                        .redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/code/{registrationId}"))
                        .permitAll()
                );

        http
                .addFilterBefore(new JWTFilter(jwtUtil, jwtService, blacklistRepository), UsernamePasswordAuthenticationFilter.class);


        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        // Swagger 관련 경로들을 가장 먼저 허용!
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs",
                                "/api-docs/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 그 외 인증이 필요 없는 경로
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/oauth/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v2/member/**").permitAll()
                        .requestMatchers("/actuator/**", "/metrics/**").permitAll()

                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                );
        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session

                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000", "https://cbh.kro.kr"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L);
                    return config;
                }))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    // 계층 권한
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        return RoleHierarchyImpl.fromHierarchy(
//                """
//                        ADMIN > USER"""
//        );
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new ForwardedHeaderFilter());
        filterRegBean.setOrder(0); // 가장 먼저 실행되게 설정
        return filterRegBean;
    }
}

