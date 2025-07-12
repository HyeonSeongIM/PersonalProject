package project.personalproject.domain.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Member")
@Schema(name = "유저 정보", description = "유저 정보를 기록하는 엔티티입니다.")
public class Member {

    /**
     * 회원 고유 ID
     * 예시 : 123
     */
    @Id
    @Column(name = "member_id")
    private Long id;

    /**
     * 소셜 별 회원 고유 값
     * 예시 : kakao 12345, naver 12345
     */
    @Column(nullable = false, name = "verifyKey")
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
