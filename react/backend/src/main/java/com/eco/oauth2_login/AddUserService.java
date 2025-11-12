package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class AddUserService {

    private final UserRepository userRepository;

    @Autowired
    public AddUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addKorisnik(Korisnik korisnik){
        System.out.println("Adding korisnik: " + korisnik.getEmail() + " with role " + korisnik.getIdUloge() + "datuma " + korisnik.getDatumRegistracije());

        korisnik.setImeKorisnik("Placeholder");
        korisnik.setPrezimeKorisnik("Placeholder");
        korisnik.setProvider("Placeholder");
        korisnik.setProviderUserId("Placeholder");
        
        userRepository.save(korisnik);

    }
}
