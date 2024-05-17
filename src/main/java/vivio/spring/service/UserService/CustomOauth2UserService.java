package vivio.spring.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vivio.spring.config.CustomOauth2UserDetails;
import vivio.spring.config.GoogleUserDetails;
import vivio.spring.config.KakaoUserDetails;
import vivio.spring.config.OAuth2UserInfo;
import vivio.spring.domain.User;
import vivio.spring.domain.enums.Platform;
import vivio.spring.domain.enums.UserRole;
import vivio.spring.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        // 뒤에 진행할 다른 소셜 서비스 로그인을 위해 구분 => 구글
        if(provider.equals("google")){
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());

        }else if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }
        Platform platform = null;
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();
        switch(providerId){
            case "google":
                platform=Platform.GOOGLE;
                break;
            case "kakao" :
                platform=Platform.KAKAO;
        }
        Optional<User> findMember = memberRepository.findByEmail(email);
        User member;

        if (findMember.isEmpty()) {
            member = User.builder()
                    .email(email)
                    .name(name)
                    .platform(platform)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .build();
            memberRepository.save(member);
        } else{
            member=findMember.get();
        }

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());
    }
}
