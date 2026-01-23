package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Faktura")
public class Faktura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfaktura")
    private Long idFaktura;

    @Column(name = "datum", nullable = false)
    private LocalDate datum;

    @Column(name = "dobavljac", length = 250)
    private String dobavljac;

    @Column(name = "iznos", nullable = false, precision = 10, scale = 2)
    private BigDecimal iznos;

    @Column(name = "opis")
    private String opis;

    @Column(name = "tipfakture", nullable = false, length = 50)
    private String tipFakture; // 'prihod' ili 'rashod'

    @Column(name = "odradena", nullable = false)
    private Boolean odradena = false;

    public Boolean getOdradena() {
        return odradena;
    }

    public void setOdradena(Boolean odradena) {
        this.odradena = odradena;
    }

    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "idfirma", referencedColumnName = "idfirma"),
            @JoinColumn(name = "idklijent", referencedColumnName = "idklijent")
    })
    private Firma firma;

    public Faktura() {
    }

    public Long getIdFaktura() {
        return idFaktura;
    }

    public void setIdFaktura(Long idFaktura) {
        this.idFaktura = idFaktura;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getDobavljac() {
        return dobavljac;
    }

    public void setDobavljac(String dobavljac) {
        this.dobavljac = dobavljac;
    }

    public BigDecimal getIznos() {
        return iznos;
    }

    public void setIznos(BigDecimal iznos) {
        this.iznos = iznos;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getTipFakture() {
        return tipFakture;
    }

    public void setTipFakture(String tipFakture) {
        this.tipFakture = tipFakture;
    }

    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }
}
