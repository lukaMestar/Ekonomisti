package com.eco.oauth2_login.databaza;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FakturaRequest {

    private LocalDate datum;
    private String dobavljac;
    private BigDecimal iznos;
    private String opis;
    private String tipFakture;
    private Long idFirma;
    private Long idKlijent;

    // GETTERI I SETTERI (OBAVEZNO za Jackson)
    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }

    public String getDobavljac() { return dobavljac; }
    public void setDobavljac(String dobavljac) { this.dobavljac = dobavljac; }

    public BigDecimal getIznos() { return iznos; }
    public void setIznos(BigDecimal iznos) { this.iznos = iznos; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public String getTipFakture() { return tipFakture; }
    public void setTipFakture(String tipFakture) { this.tipFakture = tipFakture; }

    public Long getIdFirma() { return idFirma; }
    public void setIdFirma(Long idFirma) { this.idFirma = idFirma; }

    public Long getIdKlijent() { return idKlijent; }
    public void setIdKlijent(Long idKlijent) { this.idKlijent = idKlijent; }
}
