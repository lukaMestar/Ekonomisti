package com.eco.oauth2_login;

import com.eco.oauth2_login.dto.KlijentDTO;
import com.eco.oauth2_login.dto.NoviKlijentRequest;
import com.eco.oauth2_login.databaza.RacunovodaKlijent;
import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.FirmaRepository;
import com.eco.oauth2_login.databaza.KlijentRepository;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.RacunovodaKlijentRepository;
import com.eco.oauth2_login.databaza.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class RacunovodaService {

    private final FirmaRepository firmaRepository;
    private final KlijentRepository klijentRepository;
    private final RacunovodaKlijentRepository rkRepository;
    private final UserRepository userRepository;

    public RacunovodaService(
            KlijentRepository klijentRepository,
            RacunovodaKlijentRepository rkRepository,
            UserRepository userRepository, FirmaRepository firmaRepository
    ) {
        this.firmaRepository = firmaRepository;
        this.klijentRepository = klijentRepository;
        this.rkRepository = rkRepository;
        this.userRepository = userRepository;
    }

    public List<KlijentDTO> getSlobodniKlijenti() {
        List<KlijentDTO> klijenti = klijentRepository.findSlobodniKlijenti().stream()
                .map(p -> new KlijentDTO(
                        p.getId(),
                        p.getIme(),
                        p.getPrezime()
                ))
                .toList();
        klijenti.forEach(k -> System.out.println(
            k.getId() + " | " + k.getIme() + " | " + k.getPrezime()
        ));
        return klijenti;
    }

    @Transactional
    public void dodajKlijenta(
            String racunovodaEmail,
            Long klijentId,
            BigDecimal mjesecniTrosak
    ) {
        Korisnik racunovoda = userRepository.findByEmail(racunovodaEmail)
                .orElseThrow(() -> new RuntimeException("Racunovoda nije pronađen"));

        RacunovodaKlijent rk = new RacunovodaKlijent();
        rk.setIdRacunovoda(racunovoda.getIdKorisnika());
        rk.setIdKlijent(klijentId);
        rk.setMjesecniTrosakUsluge(mjesecniTrosak);

        rkRepository.save(rk);
    }

    @Transactional
    public void kreirajNovogKlijenta(
            NoviKlijentRequest req,
            OAuth2User oauthUser
    ) {
        // 1. ID trenutno prijavljenog računovođe
        String email = oauthUser.getAttribute("email");

        Korisnik racunovodja = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Racunovodja not found"));
            
        Long racunovodjaId = racunovodja.getIdKorisnika();

        // 2. safety check (email must be unique)
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Korisnik s tim emailom već postoji");
        }

        // 3. create Korisnik (Klijent role = 3)
        Korisnik korisnik = new Korisnik();
        korisnik.setImeKorisnik(req.getIme());
        korisnik.setPrezimeKorisnik(req.getPrezime());
        korisnik.setEmail(req.getEmail());
        korisnik.setProvider("local");
        korisnik.setProviderUserId(req.getEmail());
        korisnik.setIdUloge(3); // Klijent

        userRepository.save(korisnik);

        Long klijentId = korisnik.getIdKorisnika();
        // ✅ trigger automatically inserts into Klijent table

        // 4. create Firma
        Long firmaId = firmaRepository.getNextFirmaId();

        Firma firma = new Firma();
        firma.setIdFirma(firmaId);     // REQUIRED
        firma.setIdKlijent(klijentId); // REQUIRED
        firma.setNazivFirme(req.getNazivFirme());
        firma.setEmailIzvjestaj(req.getEmailIzvjestaj());
        firma.setStanjeRacuna(
                req.getPocetnoStanje() != null
                        ? req.getPocetnoStanje()
                        : BigDecimal.ZERO
        );

        firmaRepository.save(firma);

        // 5. connect Racunovodja ↔ Klijent
        RacunovodaKlijent rk = new RacunovodaKlijent();
        rk.setIdRacunovoda(racunovodjaId);
        rk.setIdKlijent(klijentId);
        rk.setMjesecniTrosakUsluge(
                req.getMjesecniTrosakUsluge() != null
                        ? req.getMjesecniTrosakUsluge()
                        : BigDecimal.ZERO
        );

        rkRepository.save(rk);
    }

}