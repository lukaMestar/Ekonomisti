package com.eco.oauth2_login.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.FirmaRepository;


@Service
public class FirmaReportService {

    private final FirmaRepository firmaRepository;

    @Autowired
    public FirmaReportService(FirmaRepository firmaRepository) {
        this.firmaRepository = firmaRepository;
    }

    public Firma vratiFirmu(Long klijentID){
        return firmaRepository.findByIdKlijent(klijentID).orElseThrow(() -> new RuntimeException("Firma ne postoji"));
    }
    
}
