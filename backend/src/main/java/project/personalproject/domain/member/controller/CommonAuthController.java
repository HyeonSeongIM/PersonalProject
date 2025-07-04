package project.personalproject.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.personalproject.domain.member.service.MemberService;
import project.personalproject.global.redis.service.impl.ReissueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/member")
public class CommonAuthController {

    private final MemberService memberService;
    private final ReissueService reissueService;

    // 로그아웃
    @DeleteMapping("/logout")
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        memberService.signOut(request, response);
    }

    // 액세스 토큰 재발급
    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        reissueService.reissue(request, response);
    }
}
