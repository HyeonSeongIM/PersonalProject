package project.personalproject.domain.member.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.personalproject.domain.member.entity.Role;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2Info implements OAuth2User {
    private final MemberInfo memberInfo;

    /**
     * 유저 이름 추출
     *
     * @return
     */
    @Override
    public String getName() {
        return memberInfo.username();
    }

    /**
     * 소셜 + 유저 이름 추출
     *
     * @return
     */
    public String getVerifyKey() {
        return memberInfo.verifyKey();
    }

    /**
     * 유저 이메일 추출
     *
     * @return
     */
    public String getEmail() {
        return memberInfo.email();
    }

    /**
     * 유저 권한 추출
     *
     * @return
     */
    public Role getRole() {
        return memberInfo.role();
    }

    public String getProvider() {return memberInfo.provider();}

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
