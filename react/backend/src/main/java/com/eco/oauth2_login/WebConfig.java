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
        if (!url.contains(".") && !url.contains("localhost") && !url.contains("://")) {
            url = url + ".onrender.com";
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        return url;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
        .allowedOrigins(getFrontendUrl())
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*")
        .allowCredentials(true);
    }
}