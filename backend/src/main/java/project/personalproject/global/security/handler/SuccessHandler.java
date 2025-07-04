package project.personalproject.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.personalproject.domain.member.dto.OAuth2Info;
import project.personalproject.domain.member.dto.TokenResponse;
import project.personalproject.global.security.service.TokenIssueService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenIssueService tokenIssueService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2Info oAuth2Info = (OAuth2Info) authentication.getPrincipal();

        TokenResponse tokenResponse = tokenIssueService.issueTokens(oAuth2Info, response);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokenResponse));

    }
}
