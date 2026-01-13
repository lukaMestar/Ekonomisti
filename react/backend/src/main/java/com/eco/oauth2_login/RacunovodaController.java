package com.eco.oauth2_login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.RacunovodaKlijentRepository;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.dto.DodajKlijentaRequest;
import com.eco.oauth2_login.dto.KlijentDTO;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/racunovoda")
public class RacunovodaController {

    private final UserRepository userRepository;
    private final RacunovodaService racunovodaService;

    public RacunovodaController(RacunovodaService racunovodaService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.racunovodaService = racunovodaService;
    }

    @Autowired
    private RacunovodaKlijentRepository racunovodaKlijentRepository;

        // Get clients connected to the logged-in accountant
    @GetMapping("/moji-klijenti")
    public List<KlijentDTO> getMojiKlijenti(Principal principal, @AuthenticationPrincipal OAuth2User oauthUser) {
        // Principal gives logged-in user email (assuming Spring Security setup)
        String email = oauthUser.getAttribute("email");

        // TODO: map email to racunovodjaId (query Korisnici -> Racunovodja)
        Korisnik racunovoda = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Racunovoda nije pronađen"));

        return racunovodaKlijentRepository.findKlijentiByRacunovodjaId(racunovoda.getIdKorisnika());
    }

    @GetMapping("/slobodni-klijenti")
    public List<KlijentDTO> slobodniKlijenti() {
        return racunovodaService.getSlobodniKlijenti();
    }

    @PostMapping("/dodaj-klijenta")
    public ResponseEntity<Void> dodajKlijenta(
            @RequestBody DodajKlijentaRequest request,
            @AuthenticationPrincipal OAuth2User oauthUser
    ) {
        String email = oauthUser.getAttribute("email");

        racunovodaService.dodajKlijenta(
                email,
                request.getKlijentId(),
                request.getMjesecniTrosakUsluge()
        );

        return ResponseEntity.ok().build();
    }
}