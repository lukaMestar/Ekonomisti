package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Firma")
@IdClass(FirmaId.class)
public class Firma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfirma")
    private Long idFirma;

    @Id
    @Column(name = "idklijent")
    private Long idKlijent;

    @Column(name = "nazivfirme", length = 100)
    private String nazivFirme;

    @Column(name = "stanjeracuna", precision = 15, scale = 2)
    private BigDecimal stanjeRacuna = BigDecimal.ZERO;

    @Column(name = "emailizvjestaj", length = 100)
    private String emailIzvjestaj;

    @ManyToOne
    @JoinColumn(name = "idklijent", referencedColumnName = "idkorisnika", insertable = false, updatable = false)
    private Klijent klijent;

   
    public Firma() {
    }

  

    public Long getIdFirma() {
        return idFirma;
    }

    public void setIdFirma(Long idFirma) {
        this.idFirma = idFirma;
    }

    public Long getIdKlijent() {
        return idKlijent;
    }

    public void setIdKlijent(Long idKlijent) {
        this.idKlijent = idKlijent;
    }

    public String getNazivFirme() {
        return nazivFirme;
    }

    public void setNazivFirme(String nazivFirme) {
        this.nazivFirme = nazivFirme;
    }

    public BigDecimal getStanjeRacuna() {
        return stanjeRacuna;
    }

    public void setStanjeRacuna(BigDecimal stanjeRacuna) {
        this.stanjeRacuna = stanjeRacuna;
    }

    public String getEmailIzvjestaj() {
        return emailIzvjestaj;
    }

    public void setEmailIzvjestaj(String emailIzvjestaj) {
        this.emailIzvjestaj = emailIzvjestaj;
    }

    public Klijent getKlijent() {
        return klijent;
    }

    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
        if (klijent != null) {
            this.idKlijent = klijent.getIdKorisnika();
        }
    }
}