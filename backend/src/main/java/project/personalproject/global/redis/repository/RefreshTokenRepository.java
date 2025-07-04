package project.personalproject.global.redis.repository;

import org.springframework.data.repository.CrudRepository;
import project.personalproject.global.redis.domain.RefreshToken;


import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    /**
     * 리프레쉬 토큰으로 해당 RefreshToken 엔티티 값들 반환
     *
     * @param refreshToken
     * @return
     */
    Optional<RefreshToken> findById(String refreshToken);

}
