package com.eco.oauth2_login.dto;

import java.math.BigDecimal;

public class NoviKlijentRequest {

    // Korisnik / Klijent
    private String ime;
    private String prezime;
    private String email;

    // Firma
    private String nazivFirme;
    private String emailIzvjestaj;
    private BigDecimal pocetnoStanje;

    // Računovođa ↔ Klijent
    private BigDecimal mjesecniTrosakUsluge;

    // getters / setters
     public NoviKlijentRequest() {
    }

    // ===== GETTERS =====

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getEmail() {
        return email;
    }

    public String getNazivFirme() {
        return nazivFirme;
    }

    public String getEmailIzvjestaj() {
        return emailIzvjestaj;
    }

    public BigDecimal getPocetnoStanje() {
        return pocetnoStanje;
    }

    public BigDecimal getMjesecniTrosakUsluge() {
        return mjesecniTrosakUsluge;
    }

    // ===== SETTERS =====

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNazivFirme(String nazivFirme) {
        this.nazivFirme = nazivFirme;
    }

    public void setEmailIzvjestaj(String emailIzvjestaj) {
        this.emailIzvjestaj = emailIzvjestaj;
    }

    public void setPocetnoStanje(BigDecimal pocetnoStanje) {
        this.pocetnoStanje = pocetnoStanje;
    }

    public void setMjesecniTrosakUsluge(BigDecimal mjesecniTrosakUsluge) {
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
    }
}
