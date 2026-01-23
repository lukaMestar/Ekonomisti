package com.eco.oauth2_login;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.JeZaposlenDTO;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.Placa;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.databaza.ZaposlenikDTO;

@RestController
@CrossOrigin(origins = { "${FRONTEND_URL:http://localhost:5173}", "http://localhost:5173" }, allowCredentials = "true")

@RequestMapping("/api")
public class AddUserController {
    
    @Autowired
    private AddUserService addUserService;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/adduser")
    public ResponseEntity<String> addKorisnik(
            @RequestBody Korisnik korisnik,
            @AuthenticationPrincipal OAuth2User oauthUser) {
        
        if (oauthUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Korisnik nije autentificiran");
        }
        
        String email = oauthUser.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email nije dostupan");
        }
        
        Optional<Korisnik> currentUserOpt = userRepository.findByEmail(email);
        if (currentUserOpt.isEmpty() || currentUserOpt.get().getIdUloge() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Korisnik nije pronađen ili nema ulogu");
        }
        
        Korisnik currentUser = currentUserOpt.get();
        Integer currentUserRole = currentUser.getIdUloge();
        Integer newUserRole = korisnik.getIdUloge();
        
        // Provjeri dozvole ovisno o ulozi trenutnog korisnika
        if (currentUserRole == 1) {
            // Administrator može dodavati admina (1) ili računovođu (2)
            if (newUserRole == null || (newUserRole != 1 && newUserRole != 2)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Administrator može dodati samo administratora ili računovođu");
            }
        } else if (currentUserRole == 2) {
            // Računovođa može dodavati klijenta (3)
            // Napomena: Računovođa već ima endpoint /api/racunovoda/novi-klijent za dodavanje klijenta
            // Ali dozvoljavamo i ovdje za konzistentnost
            if (newUserRole == null || newUserRole != 3) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Računovođa može dodati samo klijenta");
            }
        } else if (currentUserRole == 3) {
            // Klijent može dodavati radnika (4)
            if (newUserRole == null || newUserRole != 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Klijent može dodati samo radnika");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Nemate dozvolu za dodavanje korisnika");
        }
        
        try {
            Long id = addUserService.addKorisnik(korisnik);
            System.out.println("PRIMLJENO IME: " + korisnik.getImeKorisnik());
            return ResponseEntity.ok(id.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding korisnik: " + e.getMessage());
        }
    }

    @PostMapping("/addzaposlenik")
    public ResponseEntity<String> addZaposlenik(@RequestBody ZaposlenikDTO zaposlenik) {
        try {
            System.out.println("Izvana " + zaposlenik.getIdKorisnika());
            addUserService.addZaposlenik(zaposlenik);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding zaposlenik: " + e.getMessage());
        }
    }

    @PostMapping("/addjezaposlen")
    public ResponseEntity<String> addJeZaposlen(@RequestBody JeZaposlenDTO jezaposlen) {
        try {
            addUserService.addJeZaposlen(jezaposlen);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding zaposlenik: " + e.getMessage());
        }
    }

    @PostMapping("/addplaca")
    public ResponseEntity<String> addJeZaposlen(@RequestBody Placa placa) {
        try {
            addUserService.addPlaca(placa);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding zaposlenik: " + e.getMessage());
        }
    }
}
