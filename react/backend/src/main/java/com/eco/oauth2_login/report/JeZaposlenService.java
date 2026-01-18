package com.eco.oauth2_login.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.FakturaRepository;
import com.eco.oauth2_login.databaza.JeZaposlenRepository;
import com.eco.oauth2_login.databaza.Zaposlenik;

@Service
public class JeZaposlenService {

    private final JeZaposlenRepository jeZaposlenRepository;

    @Autowired
    public JeZaposlenService(JeZaposlenRepository jeZaposlenRepository) {
        this.jeZaposlenRepository = jeZaposlenRepository;
    }

    public List<Zaposlenik> listaZaposlenikaZaFirmu(Long idFirma, long klijentId){
        return jeZaposlenRepository.findPopisZaposlenikaZaFirmu(idFirma, klijentId);
    }
    
}
