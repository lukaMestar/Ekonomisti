package com.eco.oauth2_login.report;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.PutniNalog;
import com.eco.oauth2_login.databaza.PutniNalogRepository;

@Service
public class PutniNalogReportService {
    
    private final PutniNalogRepository putniNalogRepository;
    private final OdradjeniPutniNaloziService odradjeniPutniNaloziService;

    @Autowired
    public PutniNalogReportService(PutniNalogRepository putniNalogRepository, OdradjeniPutniNaloziService odradjeniPutniNaloziService) {
        this.putniNalogRepository = putniNalogRepository;
        this.odradjeniPutniNaloziService = odradjeniPutniNaloziService;
    }

    // isto stavit univerzalno
    public List<PutniNalog> getPutniNaloziZaTekuciMjesec(Long idfirma, YearMonth mjesec) {
        LocalDate pocetakMjeseca = mjesec.atDay(1);
        LocalDate krajMjeseca = mjesec.atEndOfMonth();

        return putniNalogRepository.findPutniNaloziZaMjesec(idfirma,pocetakMjeseca, krajMjeseca);
    }

    public List<PutniNalog> getSvePutneNaloge(long idfirma){
        return putniNalogRepository.findSvePutneNaloge(idfirma);
    }

    public List<PutniNalog> getNeodradjenePutneNaloge(Long firmaid){
        List<PutniNalog> listaOdradjenihPN = odradjeniPutniNaloziService.odrPN(firmaid, firmaid);
        List<PutniNalog> listaNeodredjenihPN = this.getSvePutneNaloge(firmaid);

        Set<Long> odradjeniIds = listaOdradjenihPN.stream().map(PutniNalog::getIdPutniNalog)
                .collect(Collectors.toSet());
        listaNeodredjenihPN = listaNeodredjenihPN.stream().filter(pn -> !odradjeniIds.contains(pn.getIdPutniNalog())).toList();
        
        return listaNeodredjenihPN;
    }

    public List<PutniNalog> getRadnikPN(long idfirma, Long idklijent){
        return putniNalogRepository.radnikPN(idfirma, idklijent);
    }

}
