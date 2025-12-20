package com.eco.oauth2_login;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final UserRepository userRepository;

    @Autowired
    public RoleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Map<String, Object> me(OAuth2AuthenticationToken oauth2Token) {
        if (oauth2Token == null) {
            return Map.of(
                "role", null
            );
        }

        String email = oauth2Token.getPrincipal().getAttribute("email");

        Optional<Korisnik> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return Map.of(
                "role", null
            );
        }

        Korisnik user = userOptional.get();

        String role = switch (user.getIdUloge()) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_RACUNOVODA";
            case 3 -> "ROLE_KLIJENT";
            case 4 -> "ROLE_RADNIK";
            default -> "ROLE_USER";
        };

        return Map.of(
            "role", role
        );
    }
}