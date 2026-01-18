package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Placa")
@IdClass(PlacaId.class) // koristimo klasu za composite key
public class Placa {

    @Id
    @Column(name = "idzaposlenik")
    private Long idZaposlenik;

    @Id
    @Column(name = "idfirma")
    private Long idFirma;

    @Column(name = "iznosplace", precision = 10, scale = 2, nullable = false)
    private BigDecimal iznosPlace;

    public Placa() {}

    public Placa(Long idZaposlenik, Long idFirma, BigDecimal iznosPlace) {
        this.idZaposlenik = idZaposlenik;
        this.idFirma = idFirma;
        this.iznosPlace = iznosPlace;
    }

    // Getteri i setteri
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

    public BigDecimal getIznosPlace() {
        return iznosPlace;
    }

    public void setIznosPlace(BigDecimal iznosPlace) {
        this.iznosPlace = iznosPlace;
    }
}
