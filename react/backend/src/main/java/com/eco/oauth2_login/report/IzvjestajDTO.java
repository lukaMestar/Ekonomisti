package com.eco.oauth2_login.report;

public class IzvjestajDTO {

    private double prihodi;
    private double rashodi;
    private double pdv;
    private double dobitak;

    public IzvjestajDTO(double prihodi, double rashodi, double pdv) {
        this.prihodi = prihodi;
        this.rashodi = rashodi;
        this.pdv = pdv;
        this.dobitak = prihodi - rashodi - pdv;
    }

    public double getPrihodi() { return prihodi; }
    public double getRashodi() { return rashodi; }
    public double getPdv() { return pdv; }
    public double getDobitak() { return dobitak; }
}
