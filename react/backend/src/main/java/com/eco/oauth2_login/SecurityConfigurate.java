package com.eco.oauth2_login;

import java.io.IOException;
import java.util.List;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                
                /* .userInfoEndpoint(userInfo -> {
                System.out.println("KONFIGURIRAM userInfoEndpoint");
                userInfo.userService(new TestOAuth2UserService());})*/
                .failureHandler((request, response, exception) -> {
                    System.out.println("GREŠKA PRI LOGINU: " + exception.getMessage());

                    response.sendRedirect("http://localhost:5173/?error=unauthorized");
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
        config.addAllowedOrigin("http://localhost:5173");
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

    @Autowired
    public CustomOAuth2AuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        String email = oauth2Token.getPrincipal().getAttribute("email");

        Optional<Korisnik> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            response.sendRedirect("http://localhost:5173/?error=userNotFound");
            return;
        }
        Korisnik user = userOptional.get();
        Integer userRole = user.getIdUloge();

        System.out.println("Determiniram ulogu:");
        String redirectUrl;
        if (userRole == 1) {
            redirectUrl = "http://localhost:5173/admin";
            System.out.println("admin");
        } else if (userRole == 2) {
            System.out.println("racunovoda");
            redirectUrl = "http://localhost:5173/racunovoda";
        } else if (userRole == 3) {
            System.out.println("klijent");
            redirectUrl = "http://localhost:5173/klijent";
        } else if (userRole == 4) {
            System.out.println("radnik");
            redirectUrl = "http://localhost:5173/radnik";
        } else {
            System.out.println("fail");
            redirectUrl = "http://localhost:5173/pocetna";
        }

        response.sendRedirect(redirectUrl);
    }
}