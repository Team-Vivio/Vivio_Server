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



        // 새로운 토큰을 쿠키에 저장
        Cookie cookie = new Cookie("socialToken", token);
        cookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
        cookie.setSecure(false); // HTTP에서도 쿠키를 전송하도록 설정 (개발/테스트 환경에서만 사용)
        cookie.setPath("/"); // 쿠키의 유효 경로 설정
        cookie.setMaxAge(3600); // 쿠키의 유효 기간 설정 (초 단위, 여기서는 1시간)
        cookie.setDomain("vivi-o.site"); // 쿠키 도메인 설정

        response.addCookie(cookie); // 쿠키를 응답에 추가

        // 프론트엔드 URL로 리디렉션
        response.sendRedirect("https://www.vivi-o.site");
    }
}
