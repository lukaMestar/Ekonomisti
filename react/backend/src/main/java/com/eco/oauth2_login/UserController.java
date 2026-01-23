package com.eco.oauth2_login;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.FirmaRepository;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.PutniNalog;
import com.eco.oauth2_login.databaza.RacunovodaKlijentRepository;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.dto.KlijentDTO;

@RestController
@CrossOrigin(origins = { "${FRONTEND_URL:http://localhost:5173}", "http://localhost:5173" }, allowCredentials = "true")
public class UserController {

    private final UserRepository userRepository;
    private final FirmaRepository firmaRepository;
    private final StatusOdradjenosti statusOdradjenosti;
    private final RacunovodaKlijentRepository racunovodaKlijentRepository;

    @Autowired
    public UserController(UserRepository userRepository, FirmaRepository firmaRepository,
            StatusOdradjenosti statusOdradjenosti,
            RacunovodaKlijentRepository racunovodaKlijentRepository) {
        this.userRepository = userRepository;
        this.firmaRepository = firmaRepository;
        this.statusOdradjenosti = statusOdradjenosti;
        this.racunovodaKlijentRepository = racunovodaKlijentRepository;
    }

    @GetMapping("/api/user")
    public ResponseEntity<Map<String, Object>> user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String email = principal.getAttribute("email");
        Map<String, Object> result = new HashMap<>();

        Optional<Korisnik> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Korisnik user = userOptional.get();
            Long userId = user.getIdKorisnika();
            Integer idUloge = user.getIdUloge();
            result.put("name", user.getImeKorisnik() + " " + user.getPrezimeKorisnik());
            result.put("ime", user.getImeKorisnik());
            result.put("prezime", user.getPrezimeKorisnik());
            result.put("id", userId);
            result.put("email", email);
            result.put("oib", user.getOib());

            String role = "USER";
            if (idUloge != null) {
                switch (idUloge) {
                    case 1 -> role = "ADMIN";
                    case 2 -> role = "RACUNOVODA";
                    case 3 -> role = "KLIJENT";
                    case 4 -> role = "RADNIK";
                }
            }
            result.put("role", role);

            if (idUloge != null && idUloge == 4) {
                firmaRepository.findByRadnikId(userId).ifPresent(f -> {
                    result.put("id_firme", f.getIdFirma());
                    result.put("id_klijenta", f.getIdKlijent());
                });
            } else if (idUloge != null && idUloge == 3) {
                firmaRepository.findByIdKlijent(userId).ifPresent(f -> {
                    result.put("id_firme", f.getIdFirma());
                    result.put("id_klijenta", f.getIdKlijent());
                });
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/klijent")
    public ResponseEntity<Map<String, Object>> klijent(@AuthenticationPrincipal OAuth2User principal,
            HttpServletRequest request) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        String email = principal.getAttribute("email");
        String role = "USER";
        Long userId = 0L;
        Long firmaId = null;
        String nazivFirme = null;
        BigDecimal stanjeRacuna = null;
        String emailIzvjestaj = null;

        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                userId = user.getIdKorisnika();

                if (idUloge != null) {
                    switch (idUloge) {
                        case 1 -> role = "ADMIN";
                        case 2 -> role = "RACUNOVODA";
                        case 3 -> role = "KLIJENT";
                        case 4 -> role = "RADNIK";
                        default -> role = "USER";
                    }
                }

                if (idUloge != null && idUloge == 3) {
                    Optional<Firma> firmaOptional = firmaRepository.findByIdKlijent(userId);
                    firmaId = firmaOptional.map(Firma::getIdFirma).orElse(null);
                    nazivFirme = firmaOptional.map(Firma::getNazivFirme).orElse(null);
                    stanjeRacuna = firmaOptional.map(Firma::getStanjeRacuna).orElse(null);
                    emailIzvjestaj = firmaOptional.map(Firma::getEmailIzvjestaj).orElse(null);

                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("name", principal.getAttribute("name") != null ? principal.getAttribute("name") : "");
        result.put("email", email != null ? email : "");
        result.put("picture", principal.getAttribute("picture") != null ? principal.getAttribute("picture") : "");
        result.put("role", role);
        if (userId != 0)
            result.put("id", userId);
        if (firmaId != null)
            result.put("firmaId", firmaId);
        if (nazivFirme != null)
            result.put("nazivFirme", nazivFirme);
        if (stanjeRacuna != null)
            result.put("stanjeRacuna", stanjeRacuna);
        if (emailIzvjestaj != null)
            result.put("emailIzvjestaj", emailIzvjestaj);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/gumbOdradjeno/{id}")
    public ResponseEntity<Void> promijeniStatus(@PathVariable Long id) {
        statusOdradjenosti.promijeniStatusFaktura(id);
        statusOdradjenosti.promijeniStatusPN(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/klijenti/treba-azurirat")
    public ResponseEntity<Map<Long, Boolean>> imaNeodradjenih(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<Long, Boolean> rezultat = new HashMap<>();
        String email = principal.getAttribute("email");

        Long userId = 0L;

        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                userId = user.getIdKorisnika();

                List<KlijentDTO> listaKljenata = racunovodaKlijentRepository.findKlijentiByRacunovodjaId(userId);
                for (KlijentDTO k : listaKljenata) {
                    Long id = k.getId();
                    boolean ima = statusOdradjenosti.trebaAzurirat(id);
                    rezultat.put(id, ima);
                }
            }
        }
        return ResponseEntity.ok(rezultat);
    }

}