package com.eco.oauth2_login.report;

import java.math.BigDecimal;
import java.util.List;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.PutniNalog;

public class ReportDTO {

    private List<Faktura> listaFaktura;
    private List<PutniNalog> listaPutnihNaloga;
    private BigDecimal mjesecniTrosakUsluge;
    
    public ReportDTO(){

    }

    public ReportDTO(List<Faktura> listaFaktura, List<PutniNalog> listaPutnihNaloga, BigDecimal mjesecniTrosakUsluge) {
        this.listaFaktura = listaFaktura;
        this.listaPutnihNaloga = listaPutnihNaloga;
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
    }
    
    public List<Faktura> getListaFaktura(){return listaFaktura;};
    public List<PutniNalog> getListaPutnihNaloga(){return listaPutnihNaloga;};
    public BigDecimal getMjesecniTrosakUsluge() {return mjesecniTrosakUsluge;};

    public void setListaFaktura(List<Faktura> listaFaktura) {this.listaFaktura = listaFaktura;};
    public void setListaPutnihNaloga(List<PutniNalog> listaPutnihNaloga) {
        this.listaPutnihNaloga = listaPutnihNaloga;
    };
    public void setMjesecniTrosakUsluge(BigDecimal mjesecniTrosakUsluge) {
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
    };
   
}
