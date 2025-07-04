package project.personalproject.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.personalproject.domain.member.entity.Member;

import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    /**
     * 멤버 테이블에서 해당 verifyKey와 동일한 멤버를 반환합니다.
     *
     * @param verifyKey : kakao 1234
     * @return
     */
    Member findByVerifyKey(String verifyKey);
}
