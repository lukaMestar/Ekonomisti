package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;

@Entity
@Table(name = "Klijent")
public class Klijent {

    @Id
    @Column(name = "idkorisnika")
    private Long idKorisnika;

    @OneToOne
    @JoinColumn(name = "idkorisnika", referencedColumnName = "idkorisnika", insertable = false, updatable = false)
    private Korisnik korisnik;

   
    public Klijent() {
    }

    public Long getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(Long idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
        if (korisnik != null) {
            this.idKorisnika = korisnik.getIdKorisnika();
        }
    }
}