package com.eco.oauth2_login.report;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.RacunovodaKlijentRepository;

@Service
public class RacunovodaKlijentReportService {

    private final RacunovodaKlijentRepository racunovodaKlijentRepository;

    @Autowired
    public RacunovodaKlijentReportService(RacunovodaKlijentRepository racunovodaKlijentRepository) {
        this.racunovodaKlijentRepository = racunovodaKlijentRepository;
    }
    
    public BigDecimal getMjesecniTrosakUsluge(Long klijentId){
        return racunovodaKlijentRepository.findMjesecniTrosakUslugeByKlijentId(klijentId);
    }
    
}
