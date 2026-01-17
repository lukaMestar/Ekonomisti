package com.eco.oauth2_login;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.FirmaDTO;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

@RestController
@RequestMapping("/api")
public class RadnikController {

    private final RadnikService radnikService;
    private final UserRepository userRepository;

    public RadnikController(RadnikService radnikService, UserRepository userRepository) {
        this.radnikService = radnikService;
        this.userRepository = userRepository;
    }

    @GetMapping("/tvrtke")
    public List<FirmaDTO> mojeTvrtke(Authentication auth) {
        String email = null;

        // ako je OAuth2User
        if (auth.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
            email = (String) oAuth2User.getAttributes().get("email");
        }

        // ako je UserDetails
        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User user) {
            email = user.getUsername();
        }

        if (email == null) {
            throw new RuntimeException("Ne mogu dohvatiti email iz Authentication");
        }

        // Sada nađi korisnika u DB po emailu
        Korisnik korisnik = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"));

        return radnikService.getFirmeZaRadnika(korisnik.getIdKorisnika());
    }
}

