package project.personalproject.domain.member.dto;

import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.member.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

public record MemberInfo(
        String verifyKey,
        String username,
        String provider,
        String email,
        Role role

) {
    /**
     * Entity -> DTO 연결 코드
     *
     * @param member : membero
     * @return
     */
    public static MemberInfo of(Member member) {
        return new MemberInfo(
                member.getVerifyKey(),
                member.getUsername(),
                member.getProvider(),
                member.getEmail(),
                member.getRole()
        );
    }

    /**
     * 일반 값 -> DTO 연결 코드
     *
     * @param verifyKey
     * @param username
     * @param email
     * @param role
     * @return
     */
    public static MemberInfo to(String verifyKey, String username, String provider, String email, Role role) {
        return new MemberInfo(
                verifyKey,
                username,
                provider,
                email,
                role
        );
    }

    /**
     * 다수의 멤버를 List 형식으로 변환
     * List<Memebr> -> List<MemberInfo>
     *
     * @param members : List<Member>
     * @return
     */
    public static List<MemberInfo> listOf(List<Member> members) {
        return members.stream()
                .map(MemberInfo::of)
                .collect(Collectors.toList());
    }
}
