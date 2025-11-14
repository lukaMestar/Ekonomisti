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
        
        // If it doesn't start with http:// or https://, add https://
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            // For localhost, use http://, otherwise use https://
            if (url.contains("localhost")) {
                url = "http://" + url;
            } else {
                url = "https://" + url;
            }
        }
        
        return url;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String allowedOrigin = getFrontendUrl();
        System.out.println("++++++++++++++++++++WebConfig CORS allowed origins = " + allowedOrigin + ", https://ekonomisti-frontend.onrender.com");
        registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigin, "https://ekonomisti-frontend.onrender.com", "http://localhost:5173")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
    }
}