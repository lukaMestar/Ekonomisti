package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "jezaposlen")
@IdClass(JeZaposlenId.class)
public class JeZaposlen {

    @Id
    @Column(name = "idzaposlenik", nullable = false)
    private Long idZaposlenik;

    @Id
    @Column(name = "idfirma", nullable = false)
    private Long idFirma;

    @Id
    @Column(name = "idklijent", nullable = false)
    private Long idKlijent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "idzaposlenik",
        referencedColumnName = "idkorisnika",
        insertable = false,
        updatable = false
    )
    private Zaposlenik zaposlenik;

    @Column(name = "datumzaposljavanja", nullable = false)
    private LocalDate datumZaposljavanja = LocalDate.now();

    @Column(name = "statusaktivnosti")
    private Boolean statusAktivnosti = true;

    public JeZaposlen() {}

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

    public Zaposlenik getZaposlenik() {
        return zaposlenik;
    }

    public void setZaposlenik(Zaposlenik zaposlenik) {
        this.zaposlenik = zaposlenik;
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
