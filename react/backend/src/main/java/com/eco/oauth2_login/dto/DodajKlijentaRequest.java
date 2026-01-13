package com.eco.oauth2_login.dto;

import java.math.BigDecimal;

public class DodajKlijentaRequest {

    private Long klijentId;
    private BigDecimal mjesecniTrosakUsluge;

    public Long getKlijentId() {
        return klijentId;
    }

    public void setKlijentId(Long klijentId) {
        this.klijentId = klijentId;
    }

    public BigDecimal getMjesecniTrosakUsluge() {
        return mjesecniTrosakUsluge;
    }

    public void setMjesecniTrosakUsluge(BigDecimal mjesecniTrosakUsluge) {
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
    }
}