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
        System.out.println("[WebConfig] getFrontendUrl() - START, frontendUrl: " + frontendUrl);
        
        if (frontendUrl == null || frontendUrl.isEmpty()) {
            System.out.println("[WebConfig] getFrontendUrl() - Using default: http://localhost:5173");
            return "http://localhost:5173";
        }
        
        String url = frontendUrl;
        
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (url.contains("localhost")) {
                url = "http://" + url;
            } else {
                url = "https://" + url;
            }
            System.out.println("[WebConfig] getFrontendUrl() - Added protocol: " + url);
        }
        
        System.out.println("[WebConfig] getFrontendUrl() - END, returning: " + url);
        return url;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("[WebConfig] addCorsMappings() - START");
        String allowedOrigin = getFrontendUrl();
        System.out.println("[WebConfig] addCorsMappings() - Allowed origins: " + allowedOrigin + ", http://localhost:5173");
        registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigin, "http://localhost:5173")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
        System.out.println("[WebConfig] addCorsMappings() - END");
    }
}