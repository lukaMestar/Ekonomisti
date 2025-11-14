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
        System.out.println("[SecurityConfigurate] getFrontendUrl() - START, frontendUrl: " + frontendUrl);
        
        if (frontendUrl == null || frontendUrl.isEmpty()) {
            System.out.println("[SecurityConfigurate] getFrontendUrl() - Using default: http://localhost:5173");
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
            System.out.println("[SecurityConfigurate] getFrontendUrl() - Added protocol: " + url);
        }
        
        System.out.println("[SecurityConfigurate] getFrontendUrl() - END, returning: " + url);
        return url;
    }

    @Autowired
    public SecurityConfigurate(InvalidMail im, UserRepository userRepository) {
        System.out.println("[SecurityConfigurate] Constructor - START");
        this.im = im;
        this.userRepository = userRepository;
        System.out.println("[SecurityConfigurate] Constructor - END");
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("[SecurityConfigurate] securityFilterChain() - START");
        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .addFilterBefore(new SessionTokenFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new RequestLoggingFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
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
                        System.err.println("[SecurityConfigurate] OAuth2 failureHandler - ERROR: " + exception.getMessage());
                        if (exception.getCause() != null) {
                            System.err.println("[SecurityConfigurate] OAuth2 failureHandler - Cause: " + exception.getCause().getMessage());
                            exception.getCause().printStackTrace();
                        }
                        exception.printStackTrace();
                        
                        if (!response.isCommitted()) {
                            String redirectUrl = getFrontendUrl() + "/?error=unauthorized";
                            System.out.println("[SecurityConfigurate] OAuth2 failureHandler - Redirecting to: " + redirectUrl);
                            response.sendRedirect(redirectUrl);
                        } else {
                            System.err.println("[SecurityConfigurate] OAuth2 failureHandler - Response already committed, cannot redirect");
                        }
                    } catch (Exception e) {
                        System.err.println("[SecurityConfigurate] OAuth2 failureHandler - Error in failure handler: " + e.getMessage());
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

        System.out.println("[SecurityConfigurate] securityFilterChain() - END");
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        System.out.println("[SecurityConfigurate] corsConfigurationSource() - START");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        String allowedOrigin = getFrontendUrl();
       
        System.out.println("[SecurityConfigurate] corsConfigurationSource() - Allowed origins: " + allowedOrigin + ", https://ekonomisti-frontend.onrender.com, http://localhost:5173");
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedOrigin("https://ekonomisti-frontend.onrender.com");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of("Set-Cookie", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        System.out.println("[SecurityConfigurate] corsConfigurationSource() - END");
        return source;
    }

}

class RequestLoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[RequestLoggingFilter] init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        
        if (requestURI != null && (requestURI.contains("/oauth2/") || requestURI.contains("/login/oauth2/"))) {
            System.out.println("========================================");
            System.out.println("[RequestLoggingFilter] OAuth2 Request: " + requestURI);
            System.out.println("[RequestLoggingFilter] Method: " + httpRequest.getMethod());
            System.out.println("[RequestLoggingFilter] Query String: " + httpRequest.getQueryString());
            System.out.println("[RequestLoggingFilter] Remote Addr: " + httpRequest.getRemoteAddr());
            
            try {
                chain.doFilter(request, response);
                System.out.println("[RequestLoggingFilter] Request completed successfully");
            } catch (Exception e) {
                System.err.println("========================================");
                System.err.println("[RequestLoggingFilter] EXCEPTION CAUGHT: " + e.getClass().getName());
                System.err.println("[RequestLoggingFilter] Message: " + e.getMessage());
                System.err.println("[RequestLoggingFilter] Request URI: " + requestURI);
                e.printStackTrace();
                System.err.println("========================================");
                throw e;
            }
            System.out.println("========================================");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println("[RequestLoggingFilter] destroy()");
    }
}

class ExceptionHandlingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[ExceptionHandlingFilter] init()");
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
                System.err.println("========================================");
                System.err.println("[ExceptionHandlingFilter] UNHANDLED EXCEPTION: " + t.getClass().getName());
                System.err.println("[ExceptionHandlingFilter] Message: " + t.getMessage());
                System.err.println("[ExceptionHandlingFilter] Request URI: " + requestURI);
                t.printStackTrace();
                System.err.println("========================================");
                
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
                        System.err.println("[ExceptionHandlingFilter] Failed to redirect: " + redirectEx.getMessage());
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
        System.out.println("[ExceptionHandlingFilter] destroy()");
    }
}

class SessionTokenStore {
    private static final Map<String, SecurityContext> tokenToSecurityContext = new ConcurrentHashMap<>();
    private static final Map<String, String> sessionIdToToken = new ConcurrentHashMap<>();
    
    public static String generateToken(String sessionId, SecurityContext securityContext) {
        String token = UUID.randomUUID().toString();
        tokenToSecurityContext.put(token, securityContext);
        sessionIdToToken.put(sessionId, token);
        System.out.println("[SessionTokenStore] Generated token for session: " + sessionId);
        return token;
    }
    
    public static SecurityContext getSecurityContext(String token) {
        return tokenToSecurityContext.get(token);
    }
    
    public static void removeToken(String sessionId) {
        String token = sessionIdToToken.remove(sessionId);
        if (token != null) {
            tokenToSecurityContext.remove(token);
            System.out.println("[SessionTokenStore] Removed token for session: " + sessionId);
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
            System.out.println("[SessionTokenFilter] Found X-Session-Token header: " + token);
            SecurityContext securityContext = SessionTokenStore.getSecurityContext(token);
            if (securityContext != null) {
                System.out.println("[SessionTokenFilter] Found SecurityContext for token, restoring authentication");
                SecurityContextHolder.setContext(securityContext);
                jakarta.servlet.http.HttpSession session = httpRequest.getSession(false);
                if (session != null) {
                    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                    System.out.println("[SessionTokenFilter] Stored SecurityContext in session");
                }
            } else {
                System.out.println("[SessionTokenFilter] No SecurityContext found for token");
            }
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[SessionTokenFilter] init()");
    }
    
    @Override
    public void destroy() {
        System.out.println("[SessionTokenFilter] destroy()");
    }
}

class CookieSameSiteFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[CookieSameSiteFilter] init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            System.out.println("[CookieSameSiteFilter] doFilter() - START");
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            HttpServletResponse wrappedResponse = new jakarta.servlet.http.HttpServletResponseWrapper(httpResponse) {
                @Override
                public void setHeader(String name, String value) {
                    if ("Set-Cookie".equalsIgnoreCase(name) && value != null) {
                        try {
                            if (!value.contains("SameSite")) {
                                value = value + "; SameSite=None";
                            }
                            if (!value.contains("Secure")) {
                                value = value + "; Secure";
                            }
                            System.out.println("[CookieSameSiteFilter] setHeader() - Modified Set-Cookie: " + value);
                        } catch (Exception e) {
                            System.err.println("[CookieSameSiteFilter] setHeader() - ERROR: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    super.setHeader(name, value);
                }

                @Override
                public void addHeader(String name, String value) {
                    if ("Set-Cookie".equalsIgnoreCase(name) && value != null) {
                        try {
                            if (!value.contains("SameSite")) {
                                value = value + "; SameSite=None";
                            }
                            if (!value.contains("Secure")) {
                                value = value + "; Secure";
                            }
                            System.out.println("[CookieSameSiteFilter] addHeader() - Modified Set-Cookie: " + value);
                        } catch (Exception e) {
                            System.err.println("[CookieSameSiteFilter] addHeader() - ERROR: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    super.addHeader(name, value);
                }
            };

            chain.doFilter(request, wrappedResponse);
            System.out.println("[CookieSameSiteFilter] doFilter() - END");
        } catch (Exception e) {
            System.err.println("[CookieSameSiteFilter] doFilter() - ERROR: " + e.getMessage());
            e.printStackTrace();
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println("[CookieSameSiteFilter] destroy()");
    }
}

class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private String frontendUrl;

    @Autowired
    public CustomOAuth2AuthenticationSuccessHandler(UserRepository userRepository) {
        System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Constructor - START");
        this.userRepository = userRepository;
        this.frontendUrl = System.getenv("FRONTEND_URL");
        System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Constructor - FRONTEND_URL from env: " + this.frontendUrl);
        if (this.frontendUrl == null || this.frontendUrl.isEmpty()) {
            this.frontendUrl = "http://localhost:5173";
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Constructor - Using default: " + this.frontendUrl);
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
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Constructor - Added protocol: " + this.frontendUrl);
            }
        }
        System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Constructor - END, final frontendUrl: " + this.frontendUrl);
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] onAuthenticationSuccess() - START");
            
            jakarta.servlet.http.HttpSession session = request.getSession(false);
            if (session != null) {
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Session ID: " + session.getId());
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Session isNew: " + session.isNew());
            } else {
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] WARNING: No session found!");
            }
            
            org.springframework.security.core.context.SecurityContext securityContext = 
                org.springframework.security.core.context.SecurityContextHolder.getContext();
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] SecurityContext authentication: " + 
                (securityContext.getAuthentication() != null ? securityContext.getAuthentication().getName() : "null"));
            
            if (request.getCookies() != null) {
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Cookies count: " + request.getCookies().length);
                for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                    System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Cookie: " + cookie.getName() + " = " + cookie.getValue());
                }
            } else {
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] No cookies in request");
            }
            
            if (authentication == null) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] ERROR: Authentication is null");
                response.sendRedirect(frontendUrl + "/?error=authenticationFailed");
                return;
            }
            
            OAuth2AuthenticationToken oauth2Token;
            try {
                oauth2Token = (OAuth2AuthenticationToken) authentication;
            } catch (ClassCastException e) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] ERROR: Authentication is not OAuth2AuthenticationToken: " + e.getMessage());
                e.printStackTrace();
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=authenticationFailed");
                }
                return;
            }
            
            if (oauth2Token == null || oauth2Token.getPrincipal() == null) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] ERROR: OAuth2Token or Principal is null");
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=authenticationFailed");
                }
                return;
            }
            
            String email;
            try {
                email = oauth2Token.getPrincipal().getAttribute("email");
            } catch (Exception attrEx) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] ERROR accessing email attribute: " + attrEx.getMessage());
                attrEx.printStackTrace();
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=emailNotFound");
                }
                return;
            }
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] onAuthenticationSuccess() - Email: " + email);

            if (email == null || email.isEmpty()) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] ERROR: Email is null or empty");
                response.sendRedirect(frontendUrl + "/?error=emailNotFound");
                return;
            }

            Optional<Korisnik> userOptional;
            try {
                userOptional = userRepository.findByEmail(email);
            } catch (Exception dbException) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] Database error: " + dbException.getMessage());
                dbException.printStackTrace();
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=databaseError");
                }
                return;
            }
            
            if (userOptional.isEmpty()) {
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] onAuthenticationSuccess() - User not found, redirecting to error page");
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=userNotFound");
                }
                return;
            }
            Korisnik user = userOptional.get();
            Integer userRole = user.getIdUloge();
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] onAuthenticationSuccess() - User found, role: " + userRole);
            
            if (userRole == null) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] ERROR: User role is null");
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

            if (response.getHeaderNames() != null) {
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Response headers:");
                response.getHeaderNames().forEach(headerName -> {
                    if ("Set-Cookie".equalsIgnoreCase(headerName)) {
                        System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Set-Cookie: " + response.getHeader(headerName));
                    }
                });
            }
            
            if (session == null) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] ERROR: No session available!");
                response.sendRedirect(frontendUrl + "/?error=sessionError");
                return;
            }
            
            SecurityContext currentContext = SecurityContextHolder.getContext();
            SecurityContext contextCopy = new SecurityContextImpl();
            contextCopy.setAuthentication(currentContext.getAuthentication());
            String sessionToken = SessionTokenStore.generateToken(session.getId(), contextCopy);
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Generated session token: " + sessionToken);
            
            String redirectUrlWithToken = redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + "token=" + sessionToken;
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] onAuthenticationSuccess() - Redirecting to: " + redirectUrlWithToken);
            
            if (!response.isCommitted()) {
                response.sendRedirect(redirectUrlWithToken);
                System.out.println("[CustomOAuth2AuthenticationSuccessHandler] Redirect sent successfully");
            } else {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] Response already committed, cannot redirect");
            }
            System.out.println("[CustomOAuth2AuthenticationSuccessHandler] onAuthenticationSuccess() - END");
        } catch (Exception e) {
            System.err.println("[CustomOAuth2AuthenticationSuccessHandler] onAuthenticationSuccess() - ERROR: " + e.getMessage());
            e.printStackTrace();
            try {
                if (!response.isCommitted()) {
                    response.sendRedirect(frontendUrl + "/?error=serverError");
                } else {
                    System.err.println("[CustomOAuth2AuthenticationSuccessHandler] Response already committed, cannot redirect");
                }
            } catch (IOException ioException) {
                System.err.println("[CustomOAuth2AuthenticationSuccessHandler] Failed to redirect: " + ioException.getMessage());
                ioException.printStackTrace();
            }
        }
    }
}