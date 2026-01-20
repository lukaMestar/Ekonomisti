package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.JeZaposlenDTO;

import com.eco.oauth2_login.databaza.Placa;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.ZaposlenikDTO;

@RestController
@CrossOrigin(origins = { "${FRONTEND_URL:http://localhost:5173}", "http://localhost:5173" }, allowCredentials = "true")

@RequestMapping("/api")
public class AddUserController {

    @Autowired
    private AddUserService addUserService;

    @PostMapping("/adduser")
    public ResponseEntity<String> addKorisnik(@RequestBody Korisnik korisnik) {
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
