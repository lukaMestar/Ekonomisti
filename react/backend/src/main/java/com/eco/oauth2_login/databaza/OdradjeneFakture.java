package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "odradjenefakture")
@IdClass(OdradjeneFaktureId.class)
public class OdradjeneFakture {

    @Id
    @Column(name = "idfaktura")
    private Long idFaktura;

    @Id
    @Column(name = "idfirma")
    private Long idFirma;

    @Id
    @Column(name = "idklijent")
    private Long idKlijent;

    @Column(name = "datumodradjivanja")
    private LocalDateTime datumOdradjivanja = LocalDateTime.now();

    public OdradjeneFakture() {}

    public OdradjeneFakture(Long idFaktura, Long idFirma, Long idKlijent) {
        this.idFaktura = idFaktura;
        this.idFirma = idFirma;
        this.idKlijent = idKlijent;
    }

    public Long getIdFaktura() {
        return idFaktura;
    }

    public void setIdFaktura(Long idFaktura) {
        this.idFaktura = idFaktura;
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

    public LocalDateTime getDatumOdradjivanja() {
        return datumOdradjivanja;
    }

    public void setDatumOdradjivanja(LocalDateTime datumOdradjivanja) {
        this.datumOdradjivanja = datumOdradjivanja;
    }

}
