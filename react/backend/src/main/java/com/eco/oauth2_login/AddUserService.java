package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.JeZaposlen;
import com.eco.oauth2_login.databaza.JeZaposlenDTO;
import com.eco.oauth2_login.databaza.JeZaposlenRepository;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.databaza.Zaposlenik;
import com.eco.oauth2_login.databaza.ZaposlenikDTO;
import com.eco.oauth2_login.databaza.ZaposlenikRepository;

import jakarta.transaction.Transactional;
import java.time.LocalDate;


@Service
public class AddUserService {

    private final UserRepository userRepository;
    private final ZaposlenikRepository zaposlenikRepository;
    private final JeZaposlenRepository jezaposlenRepository;

    @Autowired
    public AddUserService(
        UserRepository userRepository,
        ZaposlenikRepository zaposlenikRepository,
        JeZaposlenRepository jezaposlenRepository
    ) {
        this.userRepository = userRepository;
        this.zaposlenikRepository = zaposlenikRepository;
        this.jezaposlenRepository = jezaposlenRepository;
    }

    @Transactional
    public void addKorisnik(Korisnik korisnik){
        korisnik.setImeKorisnik("Placeholder");
        korisnik.setPrezimeKorisnik("Placeholder");
        korisnik.setProvider("google");
        korisnik.setProviderUserId(korisnik.getEmail() != null ? korisnik.getEmail() : "placeholder");
        korisnik.setDatumRegistracije(LocalDate.now());
        
        userRepository.save(korisnik);
    }
    @Transactional
    public void addZaposlenik(ZaposlenikDTO dto){
        System.out.println("Unutra");
        // 1. Dohvati korisnika po ID
        System.out.println("DTO idKorisnika = " + dto.getIdKorisnika());

        Korisnik k = userRepository.findById(dto.getIdKorisnika())
                        .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"));
        System.out.println(k);
        System.out.println("AUUUU" + k.getIdKorisnika());
        // 2. Kreiraj Zaposlenik entity
        
        Zaposlenik z = new Zaposlenik();
        
        z.setKorisnik(k);
        System.out.println("2");
        // 3. Postavi placu
        
        System.out.println("3");
        z.setPlaca(dto.getPlaca());
        System.out.println("4");
        
        System.out.println("Kraj Unutra");
        // 4. Spremi
        zaposlenikRepository.save(z);   
    }
    @Transactional
    public void addJeZaposlen(JeZaposlenDTO dto){
        
        
        System.out.println("ID: " + dto.getIdZaposlenik());
        Zaposlenik k = zaposlenikRepository.findById(dto.getIdZaposlenik())
                        .orElseThrow(() -> new RuntimeException("Zaposlenik ne postoji"));
        JeZaposlen z = new JeZaposlen();
        System.out.println("PROSLO JE TESKI DIO");
        z.setIdZaposlenik(k.getIdKorisnika());
        System.out.println("PROSLO JE TESKI DIO 2");

        z.setIdFirma(dto.getIdFirma());
        z.setIdKlijent(dto.getIdKlijent());
        System.out.println("PROSLO JE TESKI DIO 3");

        z.setStatusAktivnosti(true);
        z.setDatumZaposljavanja(LocalDate.now());
        

        jezaposlenRepository.save(z);
    }
}
