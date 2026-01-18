package com.eco.oauth2_login.report;

import java.math.BigDecimal;
import java.util.List;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.PutniNalog;

public class ReportDTO {

    private List<Faktura> listaFaktura;
    private List<PutniNalog> listaPutnihNaloga;
    private BigDecimal mjesecniTrosakUsluge;
    private BigDecimal placaZaposlenika;
    
    public ReportDTO(){

    }

    public ReportDTO(List<Faktura> listaFaktura, List<PutniNalog> listaPutnihNaloga, BigDecimal mjesecniTrosakUsluge, BigDecimal placaZaposlenika) {
        this.listaFaktura = listaFaktura;
        this.listaPutnihNaloga = listaPutnihNaloga;
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
        this.placaZaposlenika = placaZaposlenika;
    }
    
    public List<Faktura> getListaFaktura(){return listaFaktura;};
    public List<PutniNalog> getListaPutnihNaloga(){return listaPutnihNaloga;};
    public BigDecimal getMjesecniTrosakUsluge() {return mjesecniTrosakUsluge;};
    public BigDecimal getPlacaZaposlenika() {return placaZaposlenika;};

    public void setListaFaktura(List<Faktura> listaFaktura) {this.listaFaktura = listaFaktura;};
    public void setListaPutnihNaloga(List<PutniNalog> listaPutnihNaloga) {
        this.listaPutnihNaloga = listaPutnihNaloga;
    };
    public void setMjesecniTrosakUsluge(BigDecimal mjesecniTrosakUsluge) {
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
    };
    public void setPlacaZaposlenika(BigDecimal placaZaposlenika){
        this.placaZaposlenika = placaZaposlenika;
    }
   
}
