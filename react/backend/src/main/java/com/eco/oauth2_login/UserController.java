package com.eco.oauth2_login;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String role = "USER"; 
        
    
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                if (idUloge != null) {
           
                    switch (idUloge) {
                        case 1 -> role = "ADMIN";
                        case 2 -> role = "RACUNOVODA";
                        case 3 -> role = "KLIJENT";
                        case 4 -> role = "RADNIK";
                        default -> role = "USER";
                    }
                }
            }
        }
        
        return Map.of(
            "name", principal.getAttribute("name") != null ? principal.getAttribute("name") : "",
            "email", email != null ? email : "",
            "picture", principal.getAttribute("picture") != null ? principal.getAttribute("picture") : "",
            "role", role
        );
    }
}