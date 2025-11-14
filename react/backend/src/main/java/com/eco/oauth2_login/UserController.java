package com.eco.oauth2_login;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

@RestController
@CrossOrigin(origins = {"${FRONTEND_URL:http://localhost:5173}", "https://ekonomisti-frontend.onrender.com", "http://localhost:5173"}, allowCredentials = "true")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/user")
    public ResponseEntity<Map<String, Object>> user(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        System.out.println("[UserController] user() - START");
        
        if (request.getCookies() != null) {
            System.out.println("[UserController] user() - Cookies count: " + request.getCookies().length);
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                System.out.println("[UserController] user() - Cookie: " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("[UserController] user() - No cookies in request");
        }
        
        if (principal == null) {
            System.out.println("[UserController] user() - ERROR: principal is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "User not authenticated"));
        }
        
        String email = principal.getAttribute("email");
        System.out.println("[UserController] user() - Email: " + email);
        String role = "USER";
        
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                System.out.println("[UserController] user() - User found, idUloge: " + idUloge);
                if (idUloge != null) {
                    switch (idUloge) {
                        case 1 -> role = "ADMIN";
                        case 2 -> role = "RACUNOVODA";
                        case 3 -> role = "KLIJENT";
                        case 4 -> role = "RADNIK";
                        default -> role = "USER";
                    }
                }
            } else {
                System.out.println("[UserController] user() - User not found in database");
            }
        }
        
        System.out.println("[UserController] user() - Final role: " + role);
        
        Map<String, Object> result = new HashMap<>();
        result.put("name", principal.getAttribute("name") != null ? principal.getAttribute("name") : "");
        result.put("email", email != null ? email : "");
        result.put("picture", principal.getAttribute("picture") != null ? principal.getAttribute("picture") : "");
        result.put("role", role);
        
        System.out.println("[UserController] user() - END, returning result");
        return ResponseEntity.ok(result);
    }
}