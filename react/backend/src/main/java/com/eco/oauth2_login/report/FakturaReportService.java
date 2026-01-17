package com.eco.oauth2_login.report;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.FakturaRepository;

@Service
public class FakturaReportService {
    private final FakturaRepository fakturaRepository;

    @Autowired
    public FakturaReportService(FakturaRepository fakturaRepository) {
        this.fakturaRepository = fakturaRepository;
    }

    // ovo tu jos trebam promjenit da mi salje za koji mjesec tocno zeli izvjestaj
    public List<Faktura> getFaktureZaMjesec(Long idklijent, Long idfirma, YearMonth mjesec) {
        LocalDate pocetakMjeseca = mjesec.atDay(1);
        LocalDate krajMjeseca = mjesec.atEndOfMonth();

        return fakturaRepository.findFaktureZaMjesec(idklijent, idfirma, pocetakMjeseca, krajMjeseca);
    }
}
