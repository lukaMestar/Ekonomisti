package com.eco.oauth2_login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public void handleOAuth2AuthenticationException(
            OAuth2AuthenticationException ex,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        if (requestURI != null && (requestURI.contains("/oauth2/") || requestURI.contains("/login/oauth2/"))) {
            String frontendUrl = System.getenv("FRONTEND_URL");
            if (frontendUrl == null || frontendUrl.isEmpty()) {
                frontendUrl = "http://localhost:5173";
            } else if (!frontendUrl.startsWith("http://") && !frontendUrl.startsWith("https://")) {
                if (frontendUrl.contains("localhost")) {
                    frontendUrl = "http://" + frontendUrl;
                } else {
                    frontendUrl = "https://" + frontendUrl;
                }
            }
            
            if (!response.isCommitted()) {
                response.sendRedirect(frontendUrl + "/?error=oauth2Error");
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        
        if (requestURI != null && (requestURI.contains("/oauth2/") || requestURI.contains("/login/oauth2/"))) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Internal server error\"}");
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\":\"An unexpected error occurred\"}");
    }
}

