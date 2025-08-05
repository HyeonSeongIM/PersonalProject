package project.personalproject.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.personalproject.domain.member.repository.MemberRepository;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class MemberIndexPerformanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("인덱스 미 적용")
    void not_index_repository() {
        // given
        String provider = "naver";

        // warm-up
        for (int i = 0; i < 100; i++) {
            memberRepository.findByVerifyKey(provider + i);
        }

        // when
        log.info("[not_index_repository] Test Start");
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            memberRepository.findByVerifyKey(provider + i);
        }

        log.info("[not_index_repository] Test End");
        long end = System.currentTimeMillis();

        // then
        log.info("[not_index_repository] Total time: " + (end - start) + "ms");
    }
}
