package com.eco.oauth2_login;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.http.SessionCreationPolicy;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

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
        
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (url.contains("localhost")) {
                url = "http://" + url;
            } else {
                if (!url.contains(".") && !url.contains("localhost")) {
                    url = "https://" + url + ".onrender.com";
                } else {
                    url = "https://" + url;
                }
            }
        }
        
        return url;
    }

    @Autowired
    public SecurityConfigurate(InvalidMail im, UserRepository userRepository) {
        this.im = im;
        this.userRepository = userRepository;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .addFilterBefore(new SessionTokenFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new ExceptionHandlingFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new CookieSameSiteFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation(sf -> sf.changeSessionId()) // Change session ID on authentication (default behavior)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/oauth2/**", "/login/oauth2/**", "/logout").permitAll()
                .requestMatchers("OPTIONS", "/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> {
                    userInfo.oidcUserService(im);
                })
                .failureHandler((request, response, exception) -> {
                    try {
                        if (!response.isCommitted()) {
                            String redirectUrl = getFrontendUrl() + "/?error=unauthorized";
                            response.sendRedirect(redirectUrl);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .successHandler(new CustomOAuth2AuthenticationSuccessHandler(userRepository))
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

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        String allowedOrigin = getFrontendUrl();
       
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedOrigin("https://ekonomisti-frontend.onrender.com");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of("Set-Cookie", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

class ExceptionHandlingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        
        if (requestURI != null && (requestURI.contains("/oauth2/") || requestURI.contains("/login/oauth2/"))) {
            try {
                chain.doFilter(request, response);
            } catch (Throwable t) {
                if (!httpResponse.isCommitted()) {
                    try {
                        String frontendUrl = System.getenv("FRONTEND_URL");
                        if (frontendUrl == null || frontendUrl.isEmpty()) {
                            frontendUrl = "https://ekonomisti-frontend.onrender.com";
                        } else if (!frontendUrl.startsWith("http://") && !frontendUrl.startsWith("https://")) {
                            frontendUrl = "https://" + frontendUrl;
                        }
                        httpResponse.sendRedirect(frontendUrl + "/?error=serverError");
                    } catch (Exception redirectEx) {
                        redirectEx.printStackTrace();
                    }
                }
                if (t instanceof IOException) {
                    throw (IOException) t;
                } else if (t instanceof ServletException) {
                    throw (ServletException) t;
                } else {
                    throw new ServletException(t);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}

class SessionTokenStore {
    private static final Map<String, SecurityContext> tokenToSecurityContext = new ConcurrentHashMap<>();
    private static final Map<String, String> sessionIdToToken = new ConcurrentHashMap<>();
    
    public static String generateToken(String sessionId, SecurityContext securityContext) {
        String token = UUID.randomUUID().toString();
        tokenToSecurityContext.put(token, securityContext);
        sessionIdToToken.put(sessionId, token);
        return token;
    }
    
    public static SecurityContext getSecurityContext(String token) {
        return tokenToSecurityContext.get(token);
    }
    
    public static void removeToken(String sessionId) {
        String token = sessionIdToToken.remove(sessionId);
        if (token != null) {
            tokenToSecurityContext.remove(token);
        }
    }
}

class SessionTokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader("X-Session-Token");
        
        if (token != null && !token.isEmpty()) {
            SecurityContext securityContext = SessionTokenStore.getSecurityContext(token);
            if (securityContext != null) {
                SecurityContextHolder.setContext(securityContext);
                jakarta.servlet.http.HttpSession session = httpRequest.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            }
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }
}

class CookieSameSiteFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            HttpServletResponse wrappedResponse = new jakarta.servlet.http.HttpServletResponseWrapper(httpResponse) {
                @Override
                public void setHeader(String name, String value) {
                    if ("Set-Cookie".equalsIgnoreCase(name) && value != null) {
                        if (!value.contains("SameSite")) {
                            value = value + "; SameSite=None";
                        }
                        if (!value.contains("Secure")) {
                            value = value + "; Secure";
                        }
                    }
                    super.setHeader(name, value);
                }

                @Override
                public void addHeader(String name, String value) {
                    if ("Set-Cookie".equalsIgnoreCase(name) && value != null) {
                        if (!value.contains("SameSite")) {
                            value = value + "; SameSite=None";
                        }
                        if (!value.contains("Secure")) {
                            value = value + "; Secure";
                        }
                    }
                    super.addHeader(name, value);
                }
            };

            chain.doFilter(request, wrappedResponse);
        } catch (Exception e) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
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
        } else {
            if (!this.frontendUrl.startsWith("http://") && !this.frontendUrl.startsWith("https://")) {
                if (this.frontendUrl.contains("localhost")) {
                    this.frontendUrl = "http://" + this.frontendUrl;
                } else {
                    if (!this.frontendUrl.contains(".") && !this.frontendUrl.contains("localhost")) {
                        this.frontendUrl = "https://" + this.frontendUrl + ".onrender.com";
                    } else {
                        this.frontendUrl = "https://" + this.frontendUrl;
                    }
                }
            }
        }
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            jakarta.servlet.http.HttpSession session = request.getSession(false);
            
            if (authentication == null) {
                response.sendRedirect(frontendUrl + "/?error=authenticationFailed");
                return;
            }
            
            OAuth2AuthenticationToken oauth2Token;
            try {
                oauth2Token = (OAuth2AuthenticationToken) authentication;
            } catch (ClassCastException e) {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=authenticationFailed");
                }
                return;
            }
            
            if (oauth2Token == null || oauth2Token.getPrincipal() == null) {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=authenticationFailed");
                }
                return;
            }
            
            String email;
            try {
                email = oauth2Token.getPrincipal().getAttribute("email");
            } catch (Exception attrEx) {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=emailNotFound");
                }
                return;
            }

            if (email == null || email.isEmpty()) {
                response.sendRedirect(frontendUrl + "/?error=emailNotFound");
                return;
            }

            Optional<Korisnik> userOptional;
            try {
                userOptional = userRepository.findByEmail(email);
            } catch (Exception dbException) {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=databaseError");
                }
                return;
            }
            
            if (userOptional.isEmpty()) {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=userNotFound");
                }
                return;
            }
            Korisnik user = userOptional.get();
            Integer userRole = user.getIdUloge();
            
            if (userRole == null) {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=invalidRole");
                }
                return;
            }

            String redirectUrl;
            if (userRole == 1) {
                redirectUrl = frontendUrl + "/admin";
            } else if (userRole == 2) {
                redirectUrl = frontendUrl + "/racunovoda";
            } else if (userRole == 3) {
                redirectUrl = frontendUrl + "/klijent";
            } else if (userRole == 4) {
                redirectUrl = frontendUrl + "/radnik";
            } else {
                redirectUrl = frontendUrl + "/pocetna";
            }
            
            if (session == null) {
                response.sendRedirect(frontendUrl + "/?error=sessionError");
                return;
            }
            
            SecurityContext currentContext = SecurityContextHolder.getContext();
            SecurityContext contextCopy = new SecurityContextImpl();
            contextCopy.setAuthentication(currentContext.getAuthentication());
            String sessionToken = SessionTokenStore.generateToken(session.getId(), contextCopy);
            
            String redirectUrlWithToken = redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + "token=" + sessionToken;
            
            if (!response.isCommitted()) {
                response.sendRedirect(redirectUrlWithToken);
            }
        } catch (Exception e) {
            try {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=serverError");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}