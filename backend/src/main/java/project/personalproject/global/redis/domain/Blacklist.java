package project.personalproject.global.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@Builder
@RedisHash(value = "Blacklist", timeToLive = 10 * 60 * 24 * 14)
public class Blacklist {

    /**
     * AccessToken으로 데이터를 저장합니다.
     * 예시 : Blacklist ejkdk.ejklejk.dklsla
     */
    @Id
    private String accessToken;

    /**
     * 사용자 고유 정보
     * 에시 : kakao 1234
     */
    private String verifyKey;

    public static Blacklist from(String verifyKey, String accessToken) {
        return Blacklist.builder()
                .verifyKey(verifyKey)
                .accessToken(accessToken)
                .build();
    }
}
