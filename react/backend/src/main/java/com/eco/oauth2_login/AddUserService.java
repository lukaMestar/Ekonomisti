package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

import jakarta.transaction.Transactional;
import java.time.LocalDate;


@Service
public class AddUserService {

    private final UserRepository userRepository;

    @Autowired
    public AddUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
