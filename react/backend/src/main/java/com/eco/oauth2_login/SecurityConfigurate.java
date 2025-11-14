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
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> {
                    System.out.println("KONFIGURIRAM userInfoEndpoint");
                    userInfo.oidcUserService(im);
                })
                .failureHandler((request, response, exception) -> {
                    System.out.println("GREŠKA PRI LOGINU: " + exception.getMessage());
                    exception.printStackTrace();
                    response.sendRedirect(frontendUrl + "/?error=unauthorized");
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

     /*public CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler() {
        return new CustomOAuth2AuthenticationSuccessHandler();
    }*/

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(frontendUrl);
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
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        String email = oauth2Token.getPrincipal().getAttribute("email");

        Optional<Korisnik> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            response.sendRedirect(frontendUrl + "/?error=userNotFound");
            return;
        }
        Korisnik user = userOptional.get();
        Integer userRole = user.getIdUloge();

        System.out.println("Determiniram ulogu:");
        String redirectUrl;
        if (userRole == 1) {
            redirectUrl = frontendUrl + "/admin";
            System.out.println("admin");
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

        response.sendRedirect(redirectUrl);
    }
}