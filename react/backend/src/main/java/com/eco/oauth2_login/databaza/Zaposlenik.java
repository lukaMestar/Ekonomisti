package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Zaposlenik")
public class Zaposlenik {

    @Id
    @Column(name = "idkorisnika")
    private Long idKorisnika;

    @Column(name = "placa", precision = 10, scale = 2)
    private BigDecimal placa;

    @OneToOne
    @JoinColumn(name = "idkorisnika", referencedColumnName = "idkorisnika", insertable = false, updatable = false)
    private Korisnik korisnik;

    public Zaposlenik() {
    }

    public Long getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(Long idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public BigDecimal getPlaca() {
        return placa;
    }

    public void setPlaca(BigDecimal placa) {
        this.placa = placa;
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