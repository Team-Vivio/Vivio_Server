package vivio.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class GptConfig {

     @Value("${openai.secret-key}")
    private String apiKey;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate template = new RestTemplate();
        template.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add(
                    "Authorization"
                    ,"Bearer " + apiKey);
            request.getHeaders().setContentType(APPLICATION_JSON);
            return execution.execute(request, body);
        });

        return template;
    }
}