package project.personalproject.global.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * access 토큰은 헤더에, refresh 토큰은 redis에 저장
 */
@Getter
@AllArgsConstructor
@Builder
@RedisHash(value = "Token", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken implements Serializable {

    /**
     * 리프레쉬 토큰 기준으로 데이터를 저장합니다.
     * 예시 : Token eljljdjsla.ewqjelk.dnkddmk
     */
    @Id
    private String refreshToken; // Refresh 토큰

    /**
     * 사용자 고유 정보
     * 예시 : kakao 1234
     */
    private String verifyKey; // 회원 ID

    /**
     * RefreshToken 빌더 형식입니다.
     *
     * @param verifyKey
     * @param refreshToken
     * @return
     */
    public static RefreshToken from(String verifyKey, String refreshToken) {
        return RefreshToken.builder()
                .verifyKey(verifyKey)
                .refreshToken(refreshToken)
                .build();
    }

}
