package vivio.spring.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import vivio.spring.domain.User;
import vivio.spring.repository.UserRepository;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.google.cloud.storage.Storage.SignUrlOption.signWith;
@Slf4j
@Component
public class JwtTokenProvider {

    private final String secretKey = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey";
    private final long validityInMilliseconds = 3600000; // 1시간
    private final UserRepository userRepository;

    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createToken(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
         Map<String, Object> attributes = oAuth2User.getAttributes();
        String username = authentication.getName();
        log.info("getAttributes : {}",oAuth2User.getAttributes());
        String email=(String) oAuth2User.getAttributes().get("email");
        if(email==null){
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email=(String) (String) kakaoAccount.get("email");
        }
        Date now = new Date();
        log.info("email: "+email);
        Optional<User> userOptional= userRepository.findByEmail(email);
        User user = userOptional.get();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .claim("id", user.getId())
                .claim("email",email)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
