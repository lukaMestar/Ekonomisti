package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PutniNalog")
public class PutniNalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i ")
    private Long idPutniNalog;

    @Column(name = "polaziste", length = 100)
    private String polaziste;

    @Column(name = "destinacija", length = 100)
    private String destinacija;

    @Column(name = "datumpolaska")
    private LocalDate datumPolaska;

    @Column(name = "datumpovratka")
    private LocalDate datumPovratka;

    @Column(name = "trosak", precision = 10, scale = 2)
    private BigDecimal trosak;

    @ManyToOne
    @JoinColumn(name = "idzaposlenik", referencedColumnName = "idkorisnika")
    private Zaposlenik zaposlenik;

    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "idfirma", referencedColumnName = "idfirma"),
        @JoinColumn(name = "idklijent", referencedColumnName = "idklijent")
    })
    private Firma firma;


    public PutniNalog() {
    }


    public Long getIdPutniNalog() {
        return idPutniNalog;
    }

    public void setIdPutniNalog(Long idPutniNalog) {
        this.idPutniNalog = idPutniNalog;
    }

    public String getPolaziste() {
        return polaziste;
    }

    public void setPolaziste(String polaziste) {
        this.polaziste = polaziste;
    }

    public String getDestinacija() {
        return destinacija;
    }

    public void setDestinacija(String destinacija) {
        this.destinacija = destinacija;
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

    public Zaposlenik getZaposlenik() {
        return zaposlenik;
    }

    public void setZaposlenik(Zaposlenik zaposlenik) {
        this.zaposlenik = zaposlenik;
    }

    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }
}
