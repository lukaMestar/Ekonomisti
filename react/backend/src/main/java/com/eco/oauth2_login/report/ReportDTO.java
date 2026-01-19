package com.eco.oauth2_login.report;

import java.math.BigDecimal;
import java.util.List;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.Placa;
import com.eco.oauth2_login.databaza.PutniNalog;

public class ReportDTO {

    private List<Faktura> listaFaktura;
    private List<PutniNalog> listaPutnihNaloga;
    private BigDecimal mjesecniTrosakUsluge;
    private List<Placa> placaZaposlenika;
    
    public ReportDTO(){

    }

    public ReportDTO(List<Faktura> listaFaktura, List<PutniNalog> listaPutnihNaloga, BigDecimal mjesecniTrosakUsluge,  List<Placa> placaZaposlenika) {
        this.listaFaktura = listaFaktura;
        this.listaPutnihNaloga = listaPutnihNaloga;
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
        this.placaZaposlenika = placaZaposlenika;
    }
    
    public List<Faktura> getListaFaktura(){return listaFaktura;};
    public List<PutniNalog> getListaPutnihNaloga(){return listaPutnihNaloga;};
    public BigDecimal getMjesecniTrosakUsluge() {return mjesecniTrosakUsluge;};
    public  List<Placa> getPlacaZaposlenika() {return placaZaposlenika;};

    public void setListaFaktura(List<Faktura> listaFaktura) {this.listaFaktura = listaFaktura;};
    public void setListaPutnihNaloga(List<PutniNalog> listaPutnihNaloga) {
        this.listaPutnihNaloga = listaPutnihNaloga;
    };
    public void setMjesecniTrosakUsluge(BigDecimal mjesecniTrosakUsluge) {
        this.mjesecniTrosakUsluge = mjesecniTrosakUsluge;
    };
    public void setPlacaZaposlenika( List<Placa> placaZaposlenika){
        this.placaZaposlenika = placaZaposlenika;
    }
   
}
