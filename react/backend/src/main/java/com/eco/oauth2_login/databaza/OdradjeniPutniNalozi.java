package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "odradjeniputninalozi")
@IdClass(OdradjeniPutniNaloziId.class)
public class OdradjeniPutniNalozi {

    @Id
    @Column(name = "idputninalog")
    private Long idPutniNalog;

    @Id
    @Column(name = "idfirma")
    private Long idFirma;

    @Id
    @Column(name = "idklijent")
    private Long idKlijent;

    @Column(name = "datumodradjivanja")
    private LocalDateTime datumOdradjivanja = LocalDateTime.now();

    public OdradjeniPutniNalozi() {}

    public OdradjeniPutniNalozi(Long idPutniNalog, Long idFirma, Long idKlijent) {
        this.idPutniNalog = idPutniNalog;
        this.idFirma = idFirma;
        this.idKlijent = idKlijent;
    }

    public Long getIdPutniNalog() {
        return idPutniNalog;
    }

    public void setIdPutniNalog(Long idPutniNalog) {
        this.idPutniNalog = idPutniNalog;
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
