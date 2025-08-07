package project.personalproject.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.personalproject.domain.member.entity.Member;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 멤버 테이블에서 해당 verifyKey와 동일한 멤버를 반환합니다.
     *
     * @param verifyKey : kakao 1234
     * @return
     */
    Member findByVerifyKey(String verifyKey);

    /**
     * 멤버 테이블에서 해당 email과 동일한 멤버를 반환한다.
     * @param email
     * @return
     */
    Member findByEmail(String email);

    /**
     * 멤버 테이블에서 해당 email과 verifyKey와 동일한 멤버를 반환한다.
     * @param verifyKey
     * @param email
     * @return
     */
    Member findByVerifyKeyAndEmail(String verifyKey, String email);
}
