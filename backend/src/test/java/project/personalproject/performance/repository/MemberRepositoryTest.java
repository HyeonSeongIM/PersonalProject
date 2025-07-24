package project.personalproject.performance.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.StopWatch;
import project.personalproject.domain.member.repository.MemberRepository;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@ActiveProfiles("test")
@Slf4j
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Sql(scripts = {"/schema.sql", "/test-data.sql"}, executionPhase = BEFORE_TEST_METHOD)
    void 인덱스_설정_안했을_때() {
        // 웜업
        for (int i = 0; i < 10; i++) {
            memberRepository.findByVerifyKey("kakao" + i);
        }

        // 측정
        StopWatch stopWatch = new StopWatch("NoIndexPerf");
        stopWatch.start("Test");

        // 측정
        // 100회 호출
        for (int i = 0; i < 100; i++) {
            memberRepository.findByVerifyKey("kakao25");
        }
        stopWatch.stop();

        log.info("[인덱스_설정_안했을_때] 평균시간 = {} ms", stopWatch.getTotalTimeMillis());
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/test-data.sql"}, executionPhase = BEFORE_TEST_METHOD)
    void 인덱스_성능_검증_verifyKey() {
        // 웜업
        for (int i = 0; i < 10; i++) {
            memberRepository.findByVerifyKey("kakao" + i);
        }

        StopWatch stopWatch = new StopWatch("IndexPerf");
        stopWatch.start("Test");

        // 측정
        // 100회 호출
        for (int i = 0; i < 100; i++) {
            memberRepository.findByVerifyKey("kakao25");
        }
        stopWatch.stop();

        log.info("[인덱스_성능_검증_verifyKey] 평균시간 = {} ms", stopWatch.getTotalTimeMillis());
    }

}