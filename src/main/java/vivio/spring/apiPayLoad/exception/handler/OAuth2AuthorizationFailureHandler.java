package vivio.spring.apiPayLoad.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class OAuth2AuthorizationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // UserAlreadyExistsException을 확인하고 메시지를 전달합니다.
        String errorMessage = "false";

        response.sendRedirect("https://www.vivi-o.site/Login?error=" + errorMessage);
    }
}
