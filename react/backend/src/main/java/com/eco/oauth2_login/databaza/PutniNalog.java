package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PutniNalog")
public class PutniNalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPutniNalog")
    private Long idPutniNalog;

    @Column(name = "polaziste", length = 100, nullable = false)
    private String polaziste;

    @Column(name = "odrediste", length = 100, nullable = false)
    private String odrediste;

    @Column(name = "datumPolaska")
    private LocalDate datumPolaska;

    @Column(name = "datumPovratka")
    private LocalDate datumPovratka;

    @Column(name = "svrhaPutovanja")
    private String svrhaPutovanja;

    @Column(name = "prijevoznoSredstvo", length = 50)
    private String prijevoznoSredstvo;

    @Column(name = "trosak", precision = 10, scale = 2)
    private BigDecimal trosak;

    @Column(name = "datumIzdavanja")
    private LocalDate datumIzdavanja;

    @ManyToOne(optional = true)
    @JoinColumns({
            @JoinColumn(name = "idFirma", referencedColumnName = "idFirma"),
            @JoinColumn(name = "idKlijent", referencedColumnName = "idKlijent")
    })
    private Firma firma;

    @ManyToOne
    @JoinColumn(name = "idZaposlenik", referencedColumnName = "idKorisnika")
    private Zaposlenik zaposlenik;

    @Transient
    private Long idFirma;

    @Transient
    private Long idKlijent;

    @Transient
    private Long idZaposlenik;

    public PutniNalog() {
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

    public Long getIdZaposlenik() {
        return idZaposlenik;
    }

    public void setIdZaposlenik(Long idZaposlenik) {
        this.idZaposlenik = idZaposlenik;
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

    public String getSvrhaPutovanja() {
        return svrhaPutovanja;
    }

    public void setSvrhaPutovanja(String svrhaPutovanja) {
        this.svrhaPutovanja = svrhaPutovanja;
    }

    public String getPrijevoznoSredstvo() {
        return prijevoznoSredstvo;
    }

    public void setPrijevoznoSredstvo(String prijevoznoSredstvo) {
        this.prijevoznoSredstvo = prijevoznoSredstvo;
    }

    public LocalDate getDatumIzdavanja() {
        return datumIzdavanja;
    }

    public void setDatumIzdavanja(LocalDate datumIzdavanja) {
        this.datumIzdavanja = datumIzdavanja;
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