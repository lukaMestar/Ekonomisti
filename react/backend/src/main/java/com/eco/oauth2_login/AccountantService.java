package com.eco.oauth2_login;

import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Korisnik;
@Service
public class AccountantService {
    public void addKorisnik(Korisnik korisnik){
        System.out.println("Adding korisnik: " + korisnik.getEmail() + " with role " + korisnik.getIdUloge());
    }
}
