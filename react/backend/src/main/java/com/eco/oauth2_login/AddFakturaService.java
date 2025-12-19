package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.FakturaRepository;
import com.eco.oauth2_login.databaza.FirmaRepository;

import jakarta.transaction.Transactional;
import com.eco.oauth2_login.databaza.FakturaRequest;
import com.eco.oauth2_login.databaza.Firma;

import java.time.LocalDate;


@Service
public class AddFakturaService {

    private final FakturaRepository fakturaRepository;
    private final FirmaRepository firmaRepository;

    @Autowired
    public AddFakturaService(
        FakturaRepository fakturaRepository,
        FirmaRepository firmaRepository
    ) {
        this.fakturaRepository = fakturaRepository;
        this.firmaRepository = firmaRepository;
    }

    @Transactional
    public void addFaktura(FakturaRequest req) {
        // ako želiš osnovnu validaciju
        Firma firma = firmaRepository
            .findByIdFirmaAndIdKlijent(req.getIdFirma(), req.getIdKlijent())
            .orElseThrow(() -> new RuntimeException("Firma ne postoji"));

        Faktura f = new Faktura();
        f.setDatum(req.getDatum());
        f.setDobavljac(req.getDobavljac());
        f.setIznos(req.getIznos());
        f.setOpis(req.getOpis());
        f.setTipFakture(req.getTipFakture());
        f.setFirma(firma); 

        fakturaRepository.save(f);
    }
}
