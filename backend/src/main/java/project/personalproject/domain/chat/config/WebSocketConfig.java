package project.personalproject.domain.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import project.personalproject.domain.member.repository.MemberRepository;
import project.personalproject.global.security.jwt.JwtUtil;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    /**
     * 웹소켓 엔드포인트 설정
     * setAllowedOrigins : 일부 접근 허용
     * setAllowedOriginPatterns : 와일드카드 패턴 허용
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*");
    }

    /**
     * Pub, Sub 설정
     * enableSimpleBroker : 구독 경로 설정
     * ex) /queue : 1대1 메시징용, /topic : 다대다 브로드캐스팅용
     * setApplicationDestinationPrefixes : 발행 경로 설정
     * setUserDestinationPrefix : 개별 사용자 목적지
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");

        registry.setApplicationDestinationPrefixes("/app");

        registry.setUserDestinationPrefix("/user");
    }

    /**
     * 변환기 설정
     * 1. 기본 JSON 형식으로 데이터 처리
     * 2. 직력, 역직렬 변환기
     * 3. JSON MINE 타입을 기본으로 사용하도록 설정
     * 4. 기본값 반환 X
     *
     * @param messageConverters
     * @return
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());

        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);

        return false;
    }

}

