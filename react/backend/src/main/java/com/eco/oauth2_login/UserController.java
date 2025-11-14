package com.eco.oauth2_login;

import java.util.HashMap;
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
@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}", allowCredentials = "true")

public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Optional<Korisnik> userOptional = userRepository.findByEmail(email);
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", principal.getAttribute("name"));
        userMap.put("email", email);
        userMap.put("picture", principal.getAttribute("picture"));
        
        if (userOptional.isPresent()) {
            Korisnik user = userOptional.get();
            Integer idUloge = user.getIdUloge();
            
            // Map role ID to role name
            String role = "USER";
            if (idUloge != null) {
                switch (idUloge) {
                    case 1:
                        role = "ADMIN";
                        break;
                    case 2:
                        role = "RACUNOVODA";
                        break;
                    case 3:
                        role = "KLIJENT";
                        break;
                    case 4:
                        role = "RADNIK";
                        break;
                }
            }
            userMap.put("role", role);
            userMap.put("idUloge", idUloge);
        } else {
            userMap.put("role", "USER");
        }
        
        return userMap;
    }
}