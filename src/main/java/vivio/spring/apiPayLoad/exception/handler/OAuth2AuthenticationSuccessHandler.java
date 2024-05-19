package vivio.spring.apiPayLoad.exception.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vivio.spring.config.JwtTokenProvider;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = jwtTokenProvider.createToken(authentication);
        System.out.println("Generated Token: " + token); // 디버그용 로그

        // 기존에 동일한 이름의 쿠키가 있는지 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("token".equals(c.getName())) {
                    c.setMaxAge(0); // 기존 쿠키 삭제
                    response.addCookie(c);
                }
            }
        }

        // 새로운 토큰을 쿠키에 저장
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
        cookie.setSecure(request.isSecure()); // HTTPS를 통해서만 전송되도록 설정 (프로덕션 환경에서 권장)
        cookie.setPath("/"); // 쿠키의 유효 경로 설정
        cookie.setMaxAge(3600); // 쿠키의 유효 기간 설정 (초 단위, 여기서는 1시간)
        cookie.setDomain(".vivi-o.site"); // 쿠키 도메인 설정

        response.addCookie(cookie);

        // 프론트엔드 URL로 리디렉션
        response.sendRedirect("https://www.vivi-o.site");
    }
}
