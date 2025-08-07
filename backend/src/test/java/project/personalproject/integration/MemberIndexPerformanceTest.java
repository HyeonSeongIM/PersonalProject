package project.personalproject.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import project.personalproject.domain.member.entity.Role;
import project.personalproject.domain.member.repository.MemberRepository;
import project.personalproject.global.security.jwt.JwtService;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class MemberIndexPerformanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("레포에서 단순 verifyKey 찾기")
    void find_verifyKey_to_repository() {
        // given
        String provider = "naver";

        // warm-up
        for (int i = 0; i < 100; i++) {
            memberRepository.findByVerifyKey(provider + i);
        }

        // when
        log.info("[not_index_repository] Test Start");
        long start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            memberRepository.findByVerifyKey(provider + i);
        }

        log.info("[not_index_repository] Test End");
        long end = System.nanoTime();

        // then
        log.info("[not_index_repository] Total time: " + (end - start) + "ms");
    }

    @Test
    @DisplayName("헤더에서 jwt 토큰 추출 후 멤버 반환")
    void jwt_parse_verifyKey_by_header() {
        // given
        String verifyKey = "google20001";

        String jwtToken = jwtService.generateAccessToken("access", verifyKey, "user70001", "user70001@example.com", Role.USER, (long) (100 * 100 * 60 * 1));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwtToken);

        // warm-up
        for (int i = 0; i < 100; i++) {
            jwtService.getMemberFromToken(request);
        }

        // when & then
        log.info("[jwt_parse_verifyKey_by_header] Test Start");
        long start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            jwtService.getMemberFromToken(request);
        }

        log.info("[jwt_parse_verifyKey_by_header] Test End");
        long end = System.nanoTime();

        // then
        log.info("[jwt_parse_verifyKey_by_header] Total time: " + (end - start));
    }

    @Test
    @DisplayName("jwt 토큰 로직 email 인덱싱 결과")
    void jwt_parse_member_by_email() {
        // given
        String jwtToken = jwtService.generateAccessToken("access", "naver20000", "user20000", "user20000@example.com", Role.USER, (long) (100 * 100 * 60 * 1));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwtToken);


        // warm-up

        for (int i = 0; i < 100; i++) {
            jwtService.getMemberFromToken(request);
        }

        // when

        log.info("[jwt_parse_member_by_email] Test Start");
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            jwtService.getMemberFromToken(request);
        }
        log.info("[jwt_parse_member_by_email] Test End");
        long end = System.nanoTime();


        // then
        log.info("[jwt_parse_member_by_email] Total time: " + (end - start));


    }
}
