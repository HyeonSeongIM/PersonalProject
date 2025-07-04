package project.personalproject.domain.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.personalproject.domain.member.dto.TokenResponse;

import java.io.IOException;

public interface MemberService {
    TokenResponse signIn(String provider, HttpServletRequest request, HttpServletResponse response) throws IOException;

    void signOut(HttpServletRequest request, HttpServletResponse response);
}
