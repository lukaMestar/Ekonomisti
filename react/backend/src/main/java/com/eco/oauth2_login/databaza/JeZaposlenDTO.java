package com.eco.oauth2_login.databaza;

import java.time.LocalDate;

public class JeZaposlenDTO {

    // ID zaposlenika (idkorisnika)
    private Long idZaposlenik;

    private Long idFirma;

    private Long idKlijent;

    private LocalDate datumZaposljavanja;

    private Boolean statusAktivnosti;

    public JeZaposlenDTO() {}

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
