package project.personalproject.domain.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "Member",
        indexes = {
                @Index(
                        name = "idx_member_verify_key",  // 인덱스 이름
                        columnList = "verify_key"         // 컬럼 이름 (JPA 매핑된 이름)
                ),
                @Index(
                        name = "idx_member_email",
                        columnList = "email"
                ),
                @Index(
                        name = "idx_member_verify_key_email",
                        columnList = "verify_key, email"
                )
        }
)
@Schema(name = "유저 정보", description = "유저 정보를 기록하는 엔티티입니다.")
public class Member {

    /**
     * 회원 고유 ID
     * 예시 : 123
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /**
     * 소셜 별 회원 고유 값
     * 예시 : kakao 12345, naver 12345
     */
    @Column(nullable = false, name = "verify_key")
    private String verifyKey;

    /**
     * 사용자 이름
     * 예시 : 임현성
     */
    @Column(nullable = false, name = "username")
    private String username;

    /**
     * 사용자 이메일
     * 예시 : jkl020627@gachon.ac.kr
     */
    @Column(nullable = false, name = "email")
    private String email;

    /**
     * 회원 권한
     * 예시 : user, admin, super_admin
     */
    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * 상태 표시
     * 예시 : 오프라인, 온라인
     */
    @Column(nullable = true, name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /**
     * 멤버 생성 빌더 패턴
     *
     * @param verifyKey
     * @param username
     * @param email
     * @param role
     * @return
     */
    public static Member from(String verifyKey, String username, String email, Role role) {
        return Member.builder()
                .verifyKey(verifyKey)
                .username(username)
                .email(email)
                .role(role)
                .status(UserStatus.ONLINE)
                .build();
    }

}
