package project.personalproject.domain.member.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "구글 DTO")
public class GoogleResponse implements OAuth2Response {

    private String sub;

    private String name;

    private String email;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return sub;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }
}


