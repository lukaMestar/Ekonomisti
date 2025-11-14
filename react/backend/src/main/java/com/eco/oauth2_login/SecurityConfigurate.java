package com.eco.oauth2_login;

import java.io.IOException;
import java.util.List;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import org.springframework.security.core.Authentication;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity

public class SecurityConfigurate {
    private final InvalidMail im;
    private final UserRepository userRepository;

    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;
    
    private String getFrontendUrl() {
        if (frontendUrl == null || frontendUrl.isEmpty()) {
            return "http://localhost:5173";
        }
        
        String url = frontendUrl;
        
        // If it's just a hostname without domain (e.g., "ekonomisti-frontend"), add .onrender.com
        if (!url.contains(".") && !url.contains("localhost") && !url.contains("://")) {
            url = url + ".onrender.com";
        }
        
        // Ensure FRONTEND_URL has protocol
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        
        return url;
    }

    @Autowired
    public SecurityConfigurate(InvalidMail im, UserRepository userRepository) {
        this.im = im;
        this.userRepository = userRepository;
        System.out.println("++++++++++++++++++++SecurityConfigurate bean kreiran!");
    }
    @Bean
    //@Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .cors(withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/oauth2/**", "/login/oauth2/**", "/logout").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    String requestPath = request.getRequestURI();
                    // Don't redirect OAuth callback URLs - let them be handled by OAuth2Login
                    if (requestPath.startsWith("/login/oauth2/code/") || requestPath.startsWith("/oauth2/")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    // For API requests, return 401 instead of redirecting
                    if (requestPath.startsWith("/api/")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"error\":\"Unauthorized\"}");
                        return;
                    }
                    // Redirect other unauthenticated requests to frontend (for browser navigation)
                    response.sendRedirect(getFrontendUrl() + "/");
                })
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> {
                    System.out.println("KONFIGURIRAM userInfoEndpoint");
                    userInfo.oidcUserService(im);
                })
                .failureHandler((request, response, exception) -> {
                    System.out.println("GREŠKA PRI LOGINU: " + exception.getMessage());
                    exception.printStackTrace();
                    response.sendRedirect(getFrontendUrl() + "/?error=unauthorized");
                })
                .successHandler(new CustomOAuth2AuthenticationSuccessHandler(userRepository))
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

     /*public CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler() {
        return new CustomOAuth2AuthenticationSuccessHandler();
    }*/

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        String allowedOrigin = getFrontendUrl();
        System.out.println("=== CORS Configuration ===");
        System.out.println("Allowed Origin: " + allowedOrigin);
        
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private String frontendUrl;

    @Autowired
    public CustomOAuth2AuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.frontendUrl = System.getenv("FRONTEND_URL");
        if (this.frontendUrl == null || this.frontendUrl.isEmpty()) {
            this.frontendUrl = "http://localhost:5173";
        }
        // If it's just a hostname without domain, add .onrender.com
        if (!this.frontendUrl.contains(".") && !this.frontendUrl.contains("localhost") && !this.frontendUrl.contains("://")) {
            this.frontendUrl = this.frontendUrl + ".onrender.com";
        }
        // Ensure FRONTEND_URL has protocol
        if (!this.frontendUrl.startsWith("http://") && !this.frontendUrl.startsWith("https://")) {
            this.frontendUrl = "https://" + this.frontendUrl;
        }
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        String email = oauth2Token.getPrincipal().getAttribute("email");
        
        System.out.println("=== OAuth2 Authentication Success ===");
        System.out.println("Email: " + email);
        System.out.println("Frontend URL: " + frontendUrl);
        System.out.println("Session ID: " + request.getSession().getId());

        Optional<Korisnik> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            System.out.println("User not found in database");
            response.sendRedirect(frontendUrl + "/?error=userNotFound");
            return;
        }
        Korisnik user = userOptional.get();
        Integer userRole = user.getIdUloge();

        System.out.println("Determiniram ulogu: " + userRole);
        String redirectUrl;
        if (userRole == 1) {
            redirectUrl = frontendUrl + "/admin";
            System.out.println("Redirecting to: " + redirectUrl);
        } else if (userRole == 2) {
            System.out.println("racunovoda");
            redirectUrl = frontendUrl + "/racunovoda";
        } else if (userRole == 3) {
            System.out.println("klijent");
            redirectUrl = frontendUrl + "/klijent";
        } else if (userRole == 4) {
            System.out.println("radnik");
            redirectUrl = frontendUrl + "/radnik";
        } else {
            System.out.println("fail");
            redirectUrl = frontendUrl + "/pocetna";
        }

        // Ensure session is saved before redirect
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", 
            org.springframework.security.core.context.SecurityContextHolder.getContext());
        
        System.out.println("Final redirect URL: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}