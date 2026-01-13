package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RacunovodjaKlijent") // DB table name stays as-is
@IdClass(RacunovodaKlijentId.class)
public class RacunovodaKlijent {

    @Id
    @Column(name = "idRacunovodja")
    private Long idRacunovoda;

    @Id
    @Column(name = "idKlijent")
    private Long idKlijent;

    @Column(name = "mjesecniTrosakUsluge")
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