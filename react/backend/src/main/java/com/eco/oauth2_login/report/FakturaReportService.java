package com.eco.oauth2_login.report;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.FakturaRepository;
import com.eco.oauth2_login.databaza.PutniNalog;

@Service
public class FakturaReportService {
    private final FakturaRepository fakturaRepository;
    private final OdradjeneFaktureService odradjeneFaktureService;

    @Autowired
    public FakturaReportService(FakturaRepository fakturaRepository, OdradjeneFaktureService odradjeneFaktureService) {
        this.fakturaRepository = fakturaRepository;
        this.odradjeneFaktureService = odradjeneFaktureService;
    }

    public List<Faktura> getFaktureZaMjesec(Long idklijent, Long idfirma, YearMonth mjesec) {
        LocalDate pocetakMjeseca = mjesec.atDay(1);
        LocalDate krajMjeseca = mjesec.atEndOfMonth();
        return fakturaRepository.findFaktureZaMjesec(idfirma, pocetakMjeseca, krajMjeseca);
    }

    public List<Faktura> getSveFakture(Long idklijent, long idfirma){
        return fakturaRepository.findSveFakture(idfirma);
    }

    public List<Faktura> getNeodradjeneFakture(Long idfirma){
        List<Faktura> listaNeodredjenihFaktura = this.getSveFakture(idfirma, idfirma);
        List<Faktura> odradjeneFakture = odradjeneFaktureService.odrF(idfirma);

        Set<Long> odradjeniIds = odradjeneFakture.stream().map(Faktura::getIdFaktura)
                        .collect(Collectors.toSet());
        listaNeodredjenihFaktura = listaNeodredjenihFaktura.stream()
                        .filter(pn -> !odradjeniIds.contains(pn.getIdFaktura()))
                        .toList();
        return listaNeodredjenihFaktura;

    }
}
