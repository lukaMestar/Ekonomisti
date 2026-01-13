package com.eco.oauth2_login;

import com.eco.oauth2_login.dto.KlijentDTO;
import com.eco.oauth2_login.dto.KlijentProjection;
import com.eco.oauth2_login.databaza.RacunovodaKlijent;
import com.eco.oauth2_login.databaza.KlijentRepository;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.RacunovodaKlijentRepository;
import com.eco.oauth2_login.databaza.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class RacunovodaService {

    private final KlijentRepository klijentRepository;
    private final RacunovodaKlijentRepository rkRepository;
    private final UserRepository userRepository;

    public RacunovodaService(
            KlijentRepository klijentRepository,
            RacunovodaKlijentRepository rkRepository,
            UserRepository userRepository
    ) {
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
}