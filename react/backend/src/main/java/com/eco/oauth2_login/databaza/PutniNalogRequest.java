package com.eco.oauth2_login.databaza;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PutniNalogRequest {

    private String polaziste;
    private String odrediste;
    private LocalDate datumPolaska;
    private LocalDate datumPovratka;
    private BigDecimal trosak;

    private Long idZaposlenik;
    private Long idFirma;
    private Long idKlijent;

    public String getPolaziste() {
        return polaziste;
    }

    public void setPolaziste(String polaziste) {
        this.polaziste = polaziste;
    }

    public String getOdrediste() {
        return odrediste;
    }

    public void setOdrediste(String odrediste) {
        this.odrediste = odrediste;
    }

    public LocalDate getDatumPolaska() {
        return datumPolaska;
    }

    public void setDatumPolaska(LocalDate datumPolaska) {
        this.datumPolaska = datumPolaska;
    }

    public LocalDate getDatumPovratka() {
        return datumPovratka;
    }

    public void setDatumPovratka(LocalDate datumPovratka) {
        this.datumPovratka = datumPovratka;
    }

    public BigDecimal getTrosak() {
        return trosak;
    }

    public void setTrosak(BigDecimal trosak) {
        this.trosak = trosak;
    }

    public Long getIdZaposlenik() {
        return idZaposlenik;
    }

    public void setIdZaposlenik(Long idZaposlenik) {
        this.idZaposlenik = idZaposlenik;
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
}
