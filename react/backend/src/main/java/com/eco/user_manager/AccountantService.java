package com.eco.user_manager;

import com.eco.oauth2_login.databaza.Korisnik;

public class AccountantService {
    public void addKorisnik(Korisnik korisnik){
        System.out.println("Adding korisnik: " + korisnik.getEmail() + " with role " + korisnik.getIdUloge());
    }
}
