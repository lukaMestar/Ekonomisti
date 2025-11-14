package com.eco.oauth2_login;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Map<String, Object> user() {
        System.out.println("=== /api/user endpoint called ===");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("ERROR: Not authenticated!");
            throw new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("Not authenticated");
        }
        
        String email = authentication.getName();
        System.out.println("Email from authentication: " + email);
        
        Optional<Korisnik> userOptional = userRepository.findByEmail(email);
        
        Map<String, Object> userMap = new HashMap<>();
        
        if (userOptional.isPresent()) {
            Korisnik user = userOptional.get();
            Integer idUloge = user.getIdUloge();
            
            System.out.println("User found in database, role ID: " + idUloge);
            
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
            
            userMap.put("name", user.getImeKorisnik() + " " + user.getPrezimeKorisnik());
            userMap.put("email", email);
            userMap.put("role", role);
            userMap.put("idUloge", idUloge);
            System.out.println("Returning user with role: " + role);
        } else {
            System.out.println("User not found in database");
            userMap.put("email", email);
            userMap.put("role", "USER");
        }
        
        return userMap;
    }
}