package com.eco.oauth2_login.databaza;

import java.math.BigDecimal;

public class ZaposlenikDTO {
    private Long idKorisnika;
    private BigDecimal placa;
    private String imeZaposlenik;

    public Long getIdKorisnika() { return idKorisnika; }
    public void setIdKorisnika(Long idKorisnika) { this.idKorisnika = idKorisnika; }

    public BigDecimal getPlaca() { return placa; }
    public void setPlaca(BigDecimal placa) { this.placa = placa; }

    public String getImeZaposlenik(){ return imeZaposlenik;}
    public void setImeZaposlenik(String imeZaposlenik) { this.imeZaposlenik = imeZaposlenik;}
}