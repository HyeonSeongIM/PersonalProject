package project.personalproject.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.personalproject.domain.member.dto.TokenResponse;
import project.personalproject.domain.member.service.MemberService;

import java.io.IOException;

/**
 * APP 에서 진행되는 소셜 로그인
 */
@RestController
@RequestMapping("/api/v2/member")
@RequiredArgsConstructor
@Slf4j
public class AppMemberController {
    private final MemberService memberService;

    // 로그인
    @PostMapping("/{provider}")
    public ResponseEntity<TokenResponse> signIn(@PathVariable("provider") String provider,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(memberService.signIn(provider, request, response));
    }

}
