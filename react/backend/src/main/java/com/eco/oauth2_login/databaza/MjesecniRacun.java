package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mjesecniracun")
public class MjesecniRacun {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idracun")
    private Long idRacun;
    
    @Column(name = "idracunovodja", nullable = false)
    private Long idRacunovodja;
    
    @Column(name = "idklijent", nullable = false)
    private Long idKlijent;
    
    @Column(name = "idfirma", nullable = false)
    private Long idFirma;
    
    @Column(name = "mjesec", nullable = false)
    private Integer mjesec;
    
    @Column(name = "godina", nullable = false)
    private Integer godina;
    
    @Column(name = "iznos", nullable = false, precision = 10, scale = 2)
    private BigDecimal iznos;
    
    @Column(name = "datumgeneriranja", nullable = false)
    private LocalDate datumGeneriranja;
    
    @Column(name = "datumroka", nullable = false)
    private LocalDate datumRoka;
    
    @Column(name = "statusplacanja", length = 20)
    private String statusPlacanja = "neplaceno";
    
    @Column(name = "mockpaymentid", length = 255)
    private String mockPaymentId;
    
    @Column(name = "stripepaymentintentid", length = 255)
    private String stripePaymentIntentId;
    
    @Column(name = "stripecustomerid", length = 255)
    private String stripeCustomerId;
    
    @Column(name = "datumplacanja")
    private LocalDate datumPlacanja;
    
    public MjesecniRacun() {
    }
    
    public Long getIdRacun() {
        return idRacun;
    }
    
    public void setIdRacun(Long idRacun) {
        this.idRacun = idRacun;
    }
    
    public Long getIdRacunovodja() {
        return idRacunovodja;
    }
    
    public void setIdRacunovodja(Long idRacunovodja) {
        this.idRacunovodja = idRacunovodja;
    }
    
    public Long getIdKlijent() {
        return idKlijent;
    }
    
    public void setIdKlijent(Long idKlijent) {
        this.idKlijent = idKlijent;
    }
    
    public Long getIdFirma() {
        return idFirma;
    }
    
    public void setIdFirma(Long idFirma) {
        this.idFirma = idFirma;
    }
    
    public Integer getMjesec() {
        return mjesec;
    }
    
    public void setMjesec(Integer mjesec) {
        this.mjesec = mjesec;
    }
    
    public Integer getGodina() {
        return godina;
    }
    
    public void setGodina(Integer godina) {
        this.godina = godina;
    }
    
    public BigDecimal getIznos() {
        return iznos;
    }
    
    public void setIznos(BigDecimal iznos) {
        this.iznos = iznos;
    }
    
    public LocalDate getDatumGeneriranja() {
        return datumGeneriranja;
    }
    
    public void setDatumGeneriranja(LocalDate datumGeneriranja) {
        this.datumGeneriranja = datumGeneriranja;
    }
    
    public LocalDate getDatumRoka() {
        return datumRoka;
    }
    
    public void setDatumRoka(LocalDate datumRoka) {
        this.datumRoka = datumRoka;
    }
    
    public String getStatusPlacanja() {
        return statusPlacanja;
    }
    
    public void setStatusPlacanja(String statusPlacanja) {
        this.statusPlacanja = statusPlacanja;
    }
    
    public String getMockPaymentId() {
        return mockPaymentId;
    }
    
    public void setMockPaymentId(String mockPaymentId) {
        this.mockPaymentId = mockPaymentId;
    }
    
    public LocalDate getDatumPlacanja() {
        return datumPlacanja;
    }
    
    public void setDatumPlacanja(LocalDate datumPlacanja) {
        this.datumPlacanja = datumPlacanja;
    }
    
    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }
    
    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
    
    public String getStripeCustomerId() {
        return stripeCustomerId;
    }
    
    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }
}
