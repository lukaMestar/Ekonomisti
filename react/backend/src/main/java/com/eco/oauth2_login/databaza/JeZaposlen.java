package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "jezaposlen")
@IdClass(JeZaposlenId.class) // Za kompozitni ključ
public class JeZaposlen {

    @Id
    @ManyToOne
    @JoinColumn(name = "idzaposlenik", referencedColumnName = "idkorisnika", nullable = false)
    private Zaposlenik zaposlenik;

    @Id
    @Column(name = "idfirma", nullable = false)
    private Long idFirma;

    @Id
    @Column(name = "idklijent", nullable = false)
    private Long idKlijent;

    @Column(name = "datumzaposljavanja", nullable = false)
    private LocalDate datumZaposljavanja = LocalDate.now();

    @Column(name = "statusaktivnosti")
    private Boolean statusAktivnosti = true;

    public JeZaposlen() {}

    public Zaposlenik getZaposlenik() {
        return zaposlenik;
    }

    public void setZaposlenik(Zaposlenik zaposlenik) {
        this.zaposlenik = zaposlenik;
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

    public LocalDate getDatumZaposljavanja() {
        return datumZaposljavanja;
    }

    public void setDatumZaposljavanja(LocalDate datumZaposljavanja) {
        this.datumZaposljavanja = datumZaposljavanja;
    }

    public Boolean getStatusAktivnosti() {
        return statusAktivnosti;
    }

    public void setStatusAktivnosti(Boolean statusAktivnosti) {
        this.statusAktivnosti = statusAktivnosti;
    }
}
