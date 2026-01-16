package com.eco.oauth2_login.dto;

import com.eco.oauth2_login.databaza.MjesecniRacun;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MjesecniRacunDTO {
    private Long idRacun;
    private Long idRacunovodja;
    private Long idKlijent;
    private Integer mjesec;
    private Integer godina;
    private BigDecimal iznos;
    private LocalDate datumGeneriranja;
    private LocalDate datumRoka;
    private String statusPlacanja;
    private LocalDate datumPlacanja;
    
    public MjesecniRacunDTO() {
    }
    
    public MjesecniRacunDTO(MjesecniRacun racun) {
        this.idRacun = racun.getIdRacun();
        this.idRacunovodja = racun.getIdRacunovodja();
        this.idKlijent = racun.getIdKlijent();
        this.mjesec = racun.getMjesec();
        this.godina = racun.getGodina();
        this.iznos = racun.getIznos();
        this.datumGeneriranja = racun.getDatumGeneriranja();
        this.datumRoka = racun.getDatumRoka();
        this.statusPlacanja = racun.getStatusPlacanja();
        this.datumPlacanja = racun.getDatumPlacanja();
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
    
    public LocalDate getDatumPlacanja() {
        return datumPlacanja;
    }
    
    public void setDatumPlacanja(LocalDate datumPlacanja) {
        this.datumPlacanja = datumPlacanja;
    }
}
