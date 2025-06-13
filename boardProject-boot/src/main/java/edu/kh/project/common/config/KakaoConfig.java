package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoConfig {

    @Value("${kakao.rest-key}")
    private String restKey;

   
    @Bean
    public WebClient kakaoClient() {
        return WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + restKey)
                .build();
    }

    /** dev CORS */
    @Bean
    public WebMvcConfigurer corsConfigurer(
            @Value("${cors.allow-origins}") String origins) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry reg) {
                reg.addMapping("/api/**")
                   .allowedOrigins(origins.split(","))
                   .allowedMethods("GET", "POST");
            }
        };
    }
}