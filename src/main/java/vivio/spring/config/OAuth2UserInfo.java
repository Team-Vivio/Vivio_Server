package vivio.spring.config;

public interface OAuth2UserInfo {
    //12
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
