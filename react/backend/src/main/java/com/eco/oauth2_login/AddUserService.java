package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eco.oauth2_login.databaza.JeZaposlen;
import com.eco.oauth2_login.databaza.Placa;
import com.eco.oauth2_login.databaza.JeZaposlenDTO;
import com.eco.oauth2_login.databaza.JeZaposlenRepository;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.databaza.Zaposlenik;
import com.eco.oauth2_login.databaza.ZaposlenikDTO;
import com.eco.oauth2_login.databaza.ZaposlenikRepository;
import com.eco.oauth2_login.databaza.PlacaRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class AddUserService {

    private final UserRepository userRepository;
    private final ZaposlenikRepository zaposlenikRepository;
    private final JeZaposlenRepository jezaposlenRepository;
    private final PlacaRepository placaRepository;

    @Autowired
    public AddUserService(
            UserRepository userRepository,
            ZaposlenikRepository zaposlenikRepository,
            JeZaposlenRepository jezaposlenRepository,
            PlacaRepository placaRepository) {
        this.userRepository = userRepository;
        this.zaposlenikRepository = zaposlenikRepository;
        this.jezaposlenRepository = jezaposlenRepository;
        this.placaRepository = placaRepository;
    }

    @Transactional
    public Long addKorisnik(Korisnik novi) {
        return userRepository.findByEmail(novi.getEmail())
                .map(postojeci -> {

                    postojeci.setImeKorisnik(novi.getImeKorisnik());
                    postojeci.setPrezimeKorisnik(novi.getPrezimeKorisnik());
                    postojeci.setIdUloge(novi.getIdUloge());
                    return userRepository.save(postojeci).getIdKorisnika();
                })
                .orElseGet(() -> {
                    if (novi.getProviderUserId() == null) {
                        novi.setProviderUserId("MANUAL_ENTRY");
                    }
                    return userRepository.save(novi).getIdKorisnika();
                });
    }

    @Transactional
    public void addZaposlenik(ZaposlenikDTO dto) {
        if (zaposlenikRepository.existsByIdKorisnika(dto.getIdKorisnika())) {
            return;
        }

        Korisnik k = userRepository.findById(dto.getIdKorisnika())
                .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"));

        Zaposlenik z = new Zaposlenik();
        z.setKorisnik(k);
        z.setPlaca(dto.getPlaca());
        zaposlenikRepository.save(z);
    }

    @Transactional
    public void addJeZaposlen(JeZaposlenDTO dto) {
        Zaposlenik k = zaposlenikRepository.findById(dto.getIdZaposlenik())
                .orElseThrow(() -> new RuntimeException("Zaposlenik ne postoji"));

        JeZaposlen z = new JeZaposlen();
        z.setIdZaposlenik(k.getIdKorisnika());
        z.setIdFirma(dto.getIdFirma());
        z.setIdKlijent(dto.getIdKlijent());
        z.setStatusAktivnosti(true);
        z.setDatumZaposljavanja(LocalDate.now());

        jezaposlenRepository.save(z);
    }

    @Transactional
    public void addPlaca(Placa p) {
        placaRepository.save(p);
    }
}