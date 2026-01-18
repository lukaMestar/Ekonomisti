package com.eco.oauth2_login.report;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.PutniNalog;
import com.eco.oauth2_login.databaza.PutniNalogRepository;

@Service
public class PutniNalogReportService {
    
    private final PutniNalogRepository putniNalogRepository;

    @Autowired
    public PutniNalogReportService(PutniNalogRepository putniNalogRepository) {
        this.putniNalogRepository = putniNalogRepository;
    }

    // isto stavit univerzalno
    public List<PutniNalog> getPutniNaloziZaTekuciMjesec(Long idklijent, Long idfirma, YearMonth mjesec) {
        LocalDate pocetakMjeseca = mjesec.atDay(1);
        LocalDate krajMjeseca = mjesec.atEndOfMonth();

        return putniNalogRepository.findPutniNaloziZaMjesec(idklijent, idfirma,pocetakMjeseca, krajMjeseca);
    }

    public List<PutniNalog> getSvePutneNaloge(Long idklijent, long idfirma){
       
        return putniNalogRepository.findSvePutneNaloge(idklijent, idfirma);
    }


}
