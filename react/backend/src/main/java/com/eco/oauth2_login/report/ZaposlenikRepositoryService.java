package com.eco.oauth2_login.report;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.ZaposlenikRepository;

@Service
public class ZaposlenikRepositoryService {
    private final ZaposlenikRepository zaposlenikRepository;

    @Autowired
    public ZaposlenikRepositoryService(ZaposlenikRepository zaposlenikRepository) {
        this.zaposlenikRepository = zaposlenikRepository;
    }
    
    public BigDecimal getPlacaZaposlenika(Long klijentId){
        return zaposlenikRepository.findPlacaByKorisnikId(klijentId);
    }
    
}
