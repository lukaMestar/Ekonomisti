package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "racunovodjaklijent")
@IdClass(RacunovodaKlijentId.class)
public class RacunovodaKlijent {

    @Id
    @Column(name = "idracunovodja")
    private Long idRacunovoda;

    @Id
    @Column(name = "idklijent")
    private Long idKlijent;

    @Column(name = "mjesecnitrosakusluge")
    private BigDecimal mjesecniTrosakUsluge;

    public Long getIdRacunovoda() {
        return idRacunovoda;
    }

    public void setIdRacunovoda(Long idRacunovoda) {
        this.idRacunovoda = idRacunovoda;
    }

    public Long getIdKlijent() {
        return idKlijent;
    }

    public void setIdKlijent(Long idKlijent) {
        this.idKlijent = idKlijent;
    }

    public BigDecimal getMjesecniTrosakUsluge() {
        return mjesecniTrosakUsluge;
    }

    public void setMjesecniTrosakUsluge(BigDecimal mjesecniTrosakUsluge) {
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
    }
}