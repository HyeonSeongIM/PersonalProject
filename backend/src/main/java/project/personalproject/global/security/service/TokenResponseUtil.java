package project.personalproject.global.security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenResponseUtil {

    public ResponseCookie createCookie(String key, String value) {

        return ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(86400)
                .sameSite("None")
                .build();
    }

    public String getRefreshToken(HttpServletRequest request) {
        String refresh = null;


        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return "오류" + HttpStatus.BAD_REQUEST;
        }

        return refresh;
    }

    public void expiredCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("refresh", null);
        expiredCookie.setPath("/");
        expiredCookie.setHttpOnly(true);
        expiredCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(expiredCookie);
    }

}
