package project.personalproject.domain.member.dto.auth;

public interface OAuth2Response {

    String getProvider(); // 제공자

    String getProviderId(); // 제공 ID

    String getEmail(); // 제공 이메일

    String getName(); // 제공 설정이름
}
