package com.eco.oauth2_login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.MjesecniRacun;
import com.eco.oauth2_login.databaza.RacunovodaKlijentRepository;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.dto.DodajKlijentaRequest;
import com.eco.oauth2_login.dto.KlijentDTO;
import com.eco.oauth2_login.dto.NoviKlijentRequest;
import com.eco.oauth2_login.MjesecniRacunService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/racunovoda")
public class RacunovodaController {

    private final UserRepository userRepository;
    private final RacunovodaService racunovodaService;
    private final MjesecniRacunService mjesecniRacunService;

    public RacunovodaController(
        RacunovodaService racunovodaService, 
        UserRepository userRepository,
        MjesecniRacunService mjesecniRacunService
    ) {
        this.userRepository = userRepository;
        this.racunovodaService = racunovodaService;
        this.mjesecniRacunService = mjesecniRacunService;
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

    @PostMapping("/novi-klijent")
    public ResponseEntity<Void> dodajNovogKlijenta(
            @RequestBody NoviKlijentRequest req,
            @AuthenticationPrincipal OAuth2User user
    ) {
        racunovodaService.kreirajNovogKlijenta(req, user);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Ručno generira račun za klijenta (za trenutni mjesec)
     */
    @PostMapping("/generiraj-racun/{klijentId}")
    public ResponseEntity<String> generirajRacun(
            @PathVariable Long klijentId,
            @AuthenticationPrincipal OAuth2User oauthUser
    ) {
        try {
            String email = oauthUser.getAttribute("email");
            Korisnik racunovoda = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Računovođa nije pronađen"));
            
            MjesecniRacun racun = mjesecniRacunService.generirajRacunZaKlijenta(
                racunovoda.getIdKorisnika(), 
                klijentId
            );
            
            return ResponseEntity.ok("Račun uspješno generiran za mjesec " + racun.getMjesec() + "/" + racun.getGodina());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Greška: " + e.getMessage());
        }
    }
    
}