package project.personalproject.domain.member.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class NaverResponse implements OAuth2Response {

    @JsonProperty("response")
    private Response response;

    @Getter
    private static class Response {
        private String id;
        private String name;
        private String email;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return response.getId();
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }

    @Override
    public String getName() {
        return response.getName();
    }

}
