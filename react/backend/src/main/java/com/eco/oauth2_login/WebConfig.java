package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;
    
    private String getFrontendUrl() {
        if (frontendUrl == null || frontendUrl.isEmpty()) {
            return "http://localhost:5173";
        }
        String url = frontendUrl;
        // Ensure it has protocol
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        return url;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String allowedOrigin = getFrontendUrl();
        registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigin, "https://ekonomisti-frontend.onrender.com")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
    }
}