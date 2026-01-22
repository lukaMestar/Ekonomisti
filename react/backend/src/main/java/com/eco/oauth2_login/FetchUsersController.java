package com.eco.oauth2_login;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin(origins = { "${FRONTEND_URL:http://localhost:5173}", "http://localhost:5173" }, allowCredentials = "true")

@RequestMapping("/api")
public class FetchUsersController {
    private final UserRepository userRepository;

    @Autowired
    public FetchUsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/fetchusers")
    public List<Korisnik> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/updateuser/{userId}")
    @Transactional
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody Korisnik updatedKorisnik,
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

        Optional<Korisnik> adminOpt = userRepository.findByEmail(email);
        if (adminOpt.isEmpty() || adminOpt.get().getIdUloge() == null || adminOpt.get().getIdUloge() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Samo administrator može ažurirati korisnike");
        }

        Optional<Korisnik> existingUserOpt = userRepository.findById(userId);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Korisnik nije pronađen");
        }

        Korisnik existingUser = existingUserOpt.get();

        if (!existingUser.getEmail().equals(updatedKorisnik.getEmail())) {
            Optional<Korisnik> emailExists = userRepository.findByEmail(updatedKorisnik.getEmail());
            if (emailExists.isPresent() && !emailExists.get().getIdKorisnika().equals(userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email već postoji");
            }
        }

        existingUser.setImeKorisnik(updatedKorisnik.getImeKorisnik());
        existingUser.setPrezimeKorisnik(updatedKorisnik.getPrezimeKorisnik());
        existingUser.setEmail(updatedKorisnik.getEmail());
        existingUser.setProvider(updatedKorisnik.getProvider());
        existingUser.setProviderUserId(updatedKorisnik.getProviderUserId());
        existingUser.setIdUloge(updatedKorisnik.getIdUloge());

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @PutMapping("/user/update-oib")
    @Transactional
    public ResponseEntity<?> updateMyOib(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal OAuth2User oauthUser) {

        if (oauthUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String email = oauthUser.getAttribute("email");
        String noviOib = payload.get("oib");

        if (noviOib == null || noviOib.length() != 11 || !noviOib.matches("\\d+")) {
            return ResponseEntity.badRequest().body("OIB mora imati točno 11 znamenki.");
        }

        Optional<Korisnik> korisnikOpt = userRepository.findByEmail(email);
        if (korisnikOpt.isPresent()) {
            Korisnik k = korisnikOpt.get();
            k.setOib(noviOib);

            userRepository.saveAndFlush(k);

            System.out.println("OIB uspješno spremljen u bazu za: " + email);
            return ResponseEntity.ok(k);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik nije pronađen.");
    }

    @DeleteMapping("/deleteuser/{userId}")
    @Transactional
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId,
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

        Optional<Korisnik> adminOpt = userRepository.findByEmail(email);
        if (adminOpt.isEmpty() || adminOpt.get().getIdUloge() == null || adminOpt.get().getIdUloge() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Samo administrator može brisati korisnike");
        }

        Optional<Korisnik> userToDelete = userRepository.findById(userId);
        if (userToDelete.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Korisnik nije pronađen");
        }

        Korisnik korisnik = userToDelete.get();

        if (korisnik.getIdKorisnika().equals(adminOpt.get().getIdKorisnika())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ne možete obrisati samog sebe");
        }

        userRepository.delete(korisnik);

        return ResponseEntity.ok("Korisnik je uspješno obrisan");
    }

}