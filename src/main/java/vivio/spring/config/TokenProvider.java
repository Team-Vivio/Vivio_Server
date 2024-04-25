package vivio.spring.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("secret") // 시크릿 키는 실제 서버의 시크릿 키와 일치해야 합니다.
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class); // 'id' 클레임을 Long 타입으로 추출
    }
}
